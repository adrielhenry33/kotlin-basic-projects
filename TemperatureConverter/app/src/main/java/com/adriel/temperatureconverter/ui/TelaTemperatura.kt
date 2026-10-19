package com.adriel.temperatureconverter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adriel.temperatureconverter.ui.theme.TemperatureConverterTheme

enum class Escala(val rotulo: String, val simbolo: String) {
    CELSIUS(
        "Celsius",
        simbolo = "°C"
    ),
    FAHRENHEIT("Fahrenheit", simbolo = "°F")
}


fun converterTemperatura(valorDigitado: String, escala: Escala): Double? {
    val temp = validarTexto(valorDigitado) ?: return null

    return when (escala) {
        Escala.CELSIUS -> (temp - 32) * 5 / 9
        Escala.FAHRENHEIT -> temp * 9 / 5 + 32
    }
}

@Composable
fun EscolhaConversao(
    modifier: Modifier,
    onSelecionadaChange: (Escala) -> Unit,
    selecionada: Escala
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        Escala.entries.forEachIndexed { i, escala ->
            SegmentedButton(
                selected = escala == selecionada,
                onClick = { onSelecionadaChange(escala) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = i,
                    count = Escala.entries.size,
                ),
            ) {
                Text(
                    escala.rotulo
                )
            }
        }
    }

}

@Composable
fun TelaTemperatura(modifier: Modifier = Modifier) {
    var escala by rememberSaveable { mutableStateOf(Escala.CELSIUS) }
    var texto by rememberSaveable { mutableStateOf("") }

    ConteudoTemperatura(
        escala = escala,
        onEscalaChange = { escala = it },
        texto = texto,
        onTextoChange = { texto = it },
        modifier = modifier,
    )
}

@Composable
fun ConteudoTemperatura(
    escala: Escala,
    onEscalaChange: (Escala) -> Unit,
    texto: String,
    onTextoChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val temperatura = converterTemperatura(texto, escala)
    val rotulo = when (escala) {
        Escala.CELSIUS -> "Converter Temperatura para Celsius"
        Escala.FAHRENHEIT -> "Converter Temperatura para Fahrenheit"
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(
            16.dp,
            Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EscolhaConversao(
            modifier = Modifier.fillMaxWidth(),
            onSelecionadaChange = onEscalaChange,
            selecionada = escala
        )
        CampoDecimal(
            valor = texto,
            onValueChange = onTextoChange,
            rotulo = rotulo,
            modifier= Modifier.fillMaxWidth()
        )
        Text(
            text = if (temperatura != null) {
                "Temperatura Convertida : ${"%.2f".format(temperatura)}${escala.simbolo}"

            } else {
                "Digite a temperatura corretamente"
            }
        )
    }
}

@Preview(showBackground = false, showSystemUi = true)
@Composable
fun ConteudoTemperaturaValidoPreview() {
    TemperatureConverterTheme {
        ConteudoTemperatura(
            escala = Escala.FAHRENHEIT,
            onEscalaChange = {},
            texto = "37",
            onTextoChange = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConteudoTemperaturaErroPreview() {
    TemperatureConverterTheme {
        ConteudoTemperatura(
            escala = Escala.CELSIUS,
            onEscalaChange = {},
            texto = "abc",
            onTextoChange = {},
        )
    }
}
