package com.example.to_do_app.ui.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import  androidx.compose.material3.OutlinedTextFieldDefaults
@Preview(showBackground = true)
@Composable
fun TelaTarefas(modifier: Modifier = Modifier) {
    val textDigitado = rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(top = 80.dp, start = 30.dp, end = 30.dp)
            .fillMaxSize(),
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(
                12.dp
            )
        ) {

            CampoTexto(
                valor = textDigitado.value,
                onTextChange = { text: String -> textDigitado.value = text },
                Modifier.weight(1f)
            )

            Button (
                onClick = {},
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.height(OutlinedTextFieldDefaults.MinHeight),
                ) { Text("Adicionar") }
        }
    }

}


