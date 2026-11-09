package com.example.guessinggame.ui.jogo

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun CampoNumero(
    valor: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange
) {

    val numero = valor.trim().toIntOrNull();
    val erro = if (valor.isEmpty()) false
                else if (numero == null) true
                else numero !in range


    OutlinedTextField(
        value = valor,
        modifier = modifier,
        label = { Text(text = "Digite um valor") },
        onValueChange = onValueChange,
        isError = erro,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

    )

}