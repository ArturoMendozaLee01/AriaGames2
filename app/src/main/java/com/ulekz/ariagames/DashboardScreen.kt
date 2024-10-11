package com.ulekz.ariagames

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    onAddToInventory: () -> Unit,
    onRemoveFromInventory: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit, // Función para cambiar entre modo claro y oscuro
    onLogout: () -> Unit,
    onConsultarTipoDeCambio: () -> Unit
) {
    // Usamos la función BackgroundWithLogo para darle consistencia a la interfaz
    BackgroundWithLogo(isDarkMode = isDarkMode, onToggleDarkMode = onToggleDarkMode) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Botón para agregar al inventario
            Button(
                onClick = { onAddToInventory() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Agregar al Inventario")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para descontar del inventario
            Button(
                onClick = { onRemoveFromInventory() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Descontar del Inventario")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para consultar el tipo de cambio
            Button(
                onClick = { onConsultarTipoDeCambio() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Consultar Tipo de Cambio")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de cerrar sesión
            Button(
                onClick = { onLogout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}
