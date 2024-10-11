package com.ulekz.ariagames

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun TipoDeCambioScreen(
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    var tipoDeCambio by remember { mutableStateOf("Cargando...") }
    var errorMessage by remember { mutableStateOf("") }

    fun consultarTipoDeCambio() {
        scope.launch {
            try {
                val apiKey = "1f5a329569365f1a251a0321f365e522bf8ff97cb6da54983a6a239ad9a82149" // Reemplaza con tu API Key válida
                val response = BanxicoRetrofitInstance.api.getTipoDeCambio(apiKey)
                if (response.bmx.series.isNotEmpty() && response.bmx.series[0].datos.isNotEmpty()) {
                    val dato = response.bmx.series[0].datos[0]
                    tipoDeCambio = "Fecha: ${dato.fecha}, Tipo de cambio: ${dato.dato}"
                } else {
                    errorMessage = "No se encontraron datos"
                }
            } catch (e: Exception) {
                errorMessage = "Error al consultar el tipo de cambio: ${e.message}"
            }
        }
    }

    BackgroundWithLogo(isDarkMode = isDarkMode, onToggleDarkMode = onToggleDarkMode) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tipo de Cambio del Dólar (Banco de México)",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            } else {
                Text(text = tipoDeCambio)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { consultarTipoDeCambio() }) {
                Text("Consultar Tipo de Cambio")
            }
        }
    }
}
