package com.adriel.temperatureconverter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun TelaGorjeta(modifier: Modifier = Modifier) {
    var texto by rememberSaveable { mutableStateOf("") };
    val valor = validarTexto(texto);


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment =
            Alignment.CenterHorizontally,
    ) {
        CampoDecimal(
            valor = texto,
            onValueChange = { texto = it },
            rotulo = "Valor da conta",
            modifier = modifier
        )
        Text(
            text = if(valor !=null){
                "Gorjeta (10%) : ${"%.2f".format(valor * 0.1)}"
            }else{
                "Digite o valor da conta"
            }

        )

    }

}