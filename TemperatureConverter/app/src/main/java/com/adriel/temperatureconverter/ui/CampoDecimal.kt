package com.adriel.temperatureconverter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
    val erro = valor.isNotEmpty() && validarTexto(valor) == null;
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