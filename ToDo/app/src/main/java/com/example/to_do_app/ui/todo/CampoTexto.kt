package com.example.to_do_app.ui.todo

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CampoTexto(valor: String, onTextChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = valor,
        onTextChange,
        label = { Text(text = "Cadastre uma tarefa") },
        modifier = modifier,
        shape = RoundedCornerShape(size = 4.dp)
    )
}