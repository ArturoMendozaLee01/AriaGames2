package com.ulekz.ariagames

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource

@Composable
fun RegisterScreen(
    onRegister: (String, String, String) -> Unit,
    onBackToLoginClick: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    BackgroundWithLogo(isDarkMode = isDarkMode, onToggleDarkMode = onToggleDarkMode) {
        var username by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        var confirmPasswordVisible by remember { mutableStateOf(false) }
        var showError by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Registrarse",
                style = MaterialTheme.typography.h4,
                color = if (isDarkMode) Color.White else Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de nombre de usuario
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isDarkMode) Color.White else Color.Black,
                    unfocusedBorderColor = if (isDarkMode) Color.LightGray else Color.Gray,
                    cursorColor = if (isDarkMode) Color.White else Color.Black,
                    textColor = if (isDarkMode) Color.White else Color.Black
                )
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

            // Campo de contraseña con visibilidad
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) {
                        painterResource(id = R.drawable.ic_visibility_off)
                    } else {
                        painterResource(id = R.drawable.ic_visibility)
                    }

                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(painter = image, contentDescription = if (passwordVisible) "Ocultar contraseña" else "Ver contraseña")
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isDarkMode) Color.White else Color.Black,
                    unfocusedBorderColor = if (isDarkMode) Color.LightGray else Color.Gray,
                    cursorColor = if (isDarkMode) Color.White else Color.Black,
                    textColor = if (isDarkMode) Color.White else Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de confirmación de contraseña con visibilidad
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) {
                        painterResource(id = R.drawable.ic_visibility_off)
                    } else {
                        painterResource(id = R.drawable.ic_visibility)
                    }

                    IconButton(onClick = {
                        confirmPasswordVisible = !confirmPasswordVisible
                    }) {
                        Icon(painter = image, contentDescription = if (confirmPasswordVisible) "Ocultar confirmación de contraseña" else "Ver confirmación de contraseña")
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = if (isDarkMode) Color.White else Color.Black,
                    unfocusedBorderColor = if (isDarkMode) Color.LightGray else Color.Gray,
                    cursorColor = if (isDarkMode) Color.White else Color.Black,
                    textColor = if (isDarkMode) Color.White else Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (showError) {
                Text("Las contraseñas no coinciden", color = MaterialTheme.colors.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Botón de registro
            Button(
                onClick = {
                    if (password == confirmPassword) {
                        onRegister(username, email, password)
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isDarkMode) Color(0xFF6200EE) else Color(0xFF3700B3)
                )
            ) {
                Text("Registrar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón estilizado para volver a la pantalla de inicio de sesión
            Button(
                onClick = { onBackToLoginClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isDarkMode) Color(0xFF6200EE) else Color(0xFF3700B3)
                )
            ) {
                Text("Volver a Iniciar Sesión", color = Color.White)
            }
        }
    }
}
