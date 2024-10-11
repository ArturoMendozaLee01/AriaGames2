package com.ulekz.ariagames

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RemoveFromInventoryScreen(
    dbHelper: UserDatabaseHelper,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    navController: NavController
) {
    val inventoryItems = remember { mutableStateListOf<Pair<String, Int>>() }
    var selectedItem by remember { mutableStateOf("") }
    var selectedQuantity by remember { mutableStateOf(1) }

    // Consultar la base de datos para obtener todos los ítems y sus cantidades
    LaunchedEffect(Unit) {
        refreshInventory(dbHelper, inventoryItems)  // Llamamos a la función para refrescar el inventario
    }

    // Aplicar el fondo, logo y botón de dark mode
    BackgroundWithLogo(isDarkMode = isDarkMode, onToggleDarkMode = onToggleDarkMode) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Selecciona el ítem a descontar",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = if (isDarkMode) Color.White else Color.Black,  // Mejor contraste para el texto
                    fontWeight = FontWeight.Bold  // Letras más visibles
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(inventoryItems) { (item, quantity) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "$item: $quantity unidades",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = if (isDarkMode) Color.LightGray else Color.DarkGray  // Mejora de visibilidad para el texto
                            )
                        )
                        Button(onClick = { selectedItem = item }) {
                            Text("Seleccionar")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedItem.isNotEmpty()) {
                Text(
                    "Cantidad a descontar para $selectedItem",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = if (isDarkMode) Color.White else Color.Black  // Mejor contraste
                    )
                )

                QuantitySelector(
                    initialQuantity = selectedQuantity,
                    onQuantityChange = { selectedQuantity = it }
                )

                Button(
                    onClick = {
                        println("Botón de descontar clickeado")
                        if (selectedItem.isNotEmpty()) {
                            // Pasa la categoría correspondiente
                            val currentQuantity = dbHelper.getItemQuantity(selectedItem, "Premios") // Cambia "Premios" a la categoría correspondiente
                            println("Cantidad actual de $selectedItem: $currentQuantity")

                            if (currentQuantity == 0) {
                                println("No hay unidades disponibles para descontar.")
                            } else if (selectedQuantity > currentQuantity) {
                                println("Cantidad seleccionada mayor a la disponible")
                            } else {
                                val newQuantity = currentQuantity - selectedQuantity
                                val adjustedQuantity = if (newQuantity < 0) 0 else newQuantity
                                val success = dbHelper.updateInventory(selectedItem, "Premios", adjustedQuantity) // Pasa la categoría correcta

                                if (success) {
                                    println("Inventario actualizado correctamente para $selectedItem")
                                    val updatedQuantity = dbHelper.getItemQuantity(selectedItem, "Premios")
                                    inventoryItems.replaceAll { if (it.first == selectedItem) selectedItem to updatedQuantity else it }
                                    selectedItem = ""  // Limpiamos el ítem seleccionado después de descontar
                                } else {
                                    println("Error al actualizar el inventario para $selectedItem")
                                }
                            }
                        } else {
                            println("Ningún ítem seleccionado para descontar")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Descontar del Inventario")
                }
            }
        }
    }
}

fun refreshInventory(dbHelper: UserDatabaseHelper, inventoryItems: MutableList<Pair<String, Int>>) {
    val db = dbHelper.readableDatabase
    val cursor = db.query(
        InventoryContract.InventoryEntry.TABLE_NAME,
        arrayOf(InventoryContract.InventoryEntry.COLUMN_NAME_ITEM, InventoryContract.InventoryEntry.COLUMN_NAME_QUANTITY),
        null, null, null, null, null
    )

    inventoryItems.clear()

    while (cursor.moveToNext()) {
        val item = cursor.getString(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_NAME_ITEM))
        val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(InventoryContract.InventoryEntry.COLUMN_NAME_QUANTITY))
        inventoryItems.add(item to quantity)
    }

    cursor.close()
}
