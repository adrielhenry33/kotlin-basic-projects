package com.adriel.temperatureconverter.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType


fun validarTexto(valor: String): Double? {
    return valor.trim()
        .replace(",", ".")
        .toDoubleOrNull()
}
@Composable
fun CampoDecimal(
    valor: String,
    onVaLueChange: (String) -> Unit,
    rotulo: String,
    modifier: Modifier
) {
    val erro = valor.isNotEmpty() && validarTexto(valor) == null
    OutlinedTextField(
        value = valor,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier,
        onValueChange =  onVaLueChange,
        label = { Text(rotulo) },
        singleLine = true,
        isError = erro,
        supportingText = if(erro){
            { Text("Valor invalido") }
        } else{null}
    )
}