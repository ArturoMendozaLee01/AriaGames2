package com.ulekz.ariagames

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.util.Log

@Composable
fun InventoryScreen(
    dbHelper: UserDatabaseHelper,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    val inventoryItems = remember { mutableStateListOf<Pair<String, Int>>() }

    fun refreshInventory() {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            InventoryContract.InventoryEntry.TABLE_NAME,
            arrayOf(InventoryContract.InventoryEntry.COLUMN_NAME_ITEM, InventoryContract.InventoryEntry.COLUMN_NAME_QUANTITY),
            null, null, null, null, null
        )

        inventoryItems.clear()  // Limpiar antes de agregar los nuevos datos

        while (cursor.moveToNext()) {
            val item = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_NAME_ITEM))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_NAME_QUANTITY))
            inventoryItems.add(item to quantity)
            Log.d("InventoryScreen", "Item: $item, Quantity: $quantity")
        }

        cursor.close()
    }

    LaunchedEffect(Unit) {
        refreshInventory()  // Actualizar inventario cada vez que se navega a esta pantalla
    }

    // Aplicamos el fondo, logo y dark mode usando BackgroundWithLogo
    BackgroundWithLogo(isDarkMode = isDarkMode, onToggleDarkMode = onToggleDarkMode) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Inventario Actual",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold) // Negritas
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (inventoryItems.isEmpty()) {
                Text(
                    text = "El inventario está vacío",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold) // Negritas
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(inventoryItems) { (item, quantity) ->
                        Text(
                            text = "$item: $quantity unidades",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold) // Negritas
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
