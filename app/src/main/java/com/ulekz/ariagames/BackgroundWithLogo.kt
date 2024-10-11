package com.ulekz.ariagames

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*

@Composable
fun BackgroundWithLogo(
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Fondo de pantalla
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Botón de Dark Mode en la esquina superior derecha
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = if (isDarkMode) "Modo Oscuro" else "Modo Claro", color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = onToggleDarkMode
                )
            }
        }

        // Logo centrado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),  // Ajuste para subir el logo
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_image),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(16.dp)
            )
        }

        // El contenido de cada pantalla
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 250.dp) // Desplazamos el contenido hacia abajo
        ) {
            content()
        }
    }
}
