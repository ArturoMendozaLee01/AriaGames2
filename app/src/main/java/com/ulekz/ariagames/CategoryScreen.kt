package com.ulekz.ariagames

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryScreen(
    onCategorySelected: (String) -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    BackgroundWithLogo(isDarkMode = isDarkMode, onToggleDarkMode = onToggleDarkMode) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Selecciona una Categoría", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para la categoría Premios
            Button(
                onClick = { onCategorySelected("Premios") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Premios")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para la categoría Extras
            Button(
                onClick = { onCategorySelected("Extras") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Extras")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para la categoría Refacciones
            Button(
                onClick = { onCategorySelected("Refacciones") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Refacciones")
            }
        }
    }
}
