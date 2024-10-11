package com.ulekz.ariagames

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    // Aquí usamos el componente BackgroundWithLogo ya existente
    BackgroundWithLogo(isDarkMode = isDarkMode, onToggleDarkMode = onToggleDarkMode) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.h5, // Usando Material 2 para mayor estabilidad
                color = if (isDarkMode) Color.White else Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de correo electrónico
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isDarkMode) Color.White else Color.Black,
                    unfocusedBorderColor = if (isDarkMode) Color.LightGray else Color.Gray,
                    cursorColor = if (isDarkMode) Color.White else Color.Black,
                    textColor = if (isDarkMode) Color.White else Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isDarkMode) Color.White else Color.Black,
                    unfocusedBorderColor = if (isDarkMode) Color.LightGray else Color.Gray,
                    cursorColor = if (isDarkMode) Color.White else Color.Black,
                    textColor = if (isDarkMode) Color.White else Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Iniciar Sesión
            Button(
                onClick = { onLogin(email, password) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isDarkMode) Color(0xFF6200EE) else Color(0xFF3700B3)
                )
            ) {
                Text("Iniciar Sesión", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Registrarse
            Button(
                onClick = { onRegisterClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isDarkMode) Color(0xFF6200EE) else Color(0xFF3700B3)
                )
            ) {
                Text("Registrarse", color = Color.White)
            }
        }
    }
}
