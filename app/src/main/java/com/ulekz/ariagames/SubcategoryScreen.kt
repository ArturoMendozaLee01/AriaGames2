package com.ulekz.ariagames

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SubcategoryScreen(
    category: String,
    onSubcategorySelected: (String, Int) -> Unit,
    dbHelper: UserDatabaseHelper,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    navController: NavController
) {
    var selectedQuantity by remember { mutableStateOf(1) }
    var selectedSubcategory by remember { mutableStateOf("") }

    BackgroundWithLogo(isDarkMode = isDarkMode, onToggleDarkMode = onToggleDarkMode) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Selecciona una Subcategoría de $category",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de subcategorías en un LazyColumn para que sea desplazable
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Para asegurarnos de que el LazyColumn ocupe el espacio disponible
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val subcategories = when (category) {
                    "Premios" -> listOf("Peluche Chico", "Peluche Mediano", "Peluche Grande", "Peluche Jumbo", "Mini Doll", "Cooper")
                    "Extras" -> listOf("Pelota Loca", "Pelota Inflatu", "Llavero Personaje", "Tickets", "Lego", "Relojes", "Bocinas")
                    "Refacciones" -> listOf("Chapas y Llaves", "Cableado Jamma", "Ticketeras", "Contadores", "Electrónico", "Mecánico", "Multimoneda", "Botón", "Palanca", "Aguja", "Original", "Copia", "6 Botones", "9 Botones", "De Refacción")
                    else -> emptyList()
                }

                items(subcategories) { subcategory ->
                    Button(
                        onClick = {
                            selectedSubcategory = subcategory
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedSubcategory == subcategory) Color.Gray else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(subcategory)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de cantidad
            QuantitySelector(
                initialQuantity = 1,
                onQuantityChange = { newQuantity ->
                    selectedQuantity = newQuantity
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para agregar al inventario
            Button(
                onClick = {
                    if (selectedSubcategory.isNotEmpty()) {
                        val currentQuantity = dbHelper.getItemQuantity(selectedSubcategory, category)
                        dbHelper.updateInventory(selectedSubcategory, category, currentQuantity + selectedQuantity)

                        navController.navigate("inventoryScreen")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Agregar al Inventario")
            }
        }
    }
}
