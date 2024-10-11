package com.ulekz.ariagames

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun QuantitySelector(
    initialQuantity: Int = 1,
    onQuantityChange: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf(initialQuantity) }

    // Validamos que la cantidad siempre esté entre 0 y 999 (ahora permitimos 0)
    fun validateQuantity(value: Int): Int {
        return when {
            value < 0 -> 0 // Permitimos que llegue a 0
            value > 999 -> 999
            else -> value
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Botón para disminuir la cantidad
        Button(onClick = {
            quantity = validateQuantity(quantity - 1)
            onQuantityChange(quantity)
        }) {
            Text("-")
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Campo de texto para ingresar la cantidad manualmente
        OutlinedTextField(
            value = quantity.toString(),
            onValueChange = {
                val newQuantity = it.toIntOrNull() ?: quantity // Si no es número, no cambia
                quantity = validateQuantity(newQuantity)
                onQuantityChange(quantity)
            },
            label = { Text("Cantidad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(120.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Botón para aumentar la cantidad
        Button(onClick = {
            quantity = validateQuantity(quantity + 1)
            onQuantityChange(quantity)
        }) {
            Text("+")
        }
    }
}

