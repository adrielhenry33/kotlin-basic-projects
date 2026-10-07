package com.adriel.temperatureconverter.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adriel.temperatureconverter.ui.theme.TemperatureConverterTheme

@Preview(showBackground = true)
@Composable
// A: background primeiro, então o fundo amarelo pinta tudo.
// O padding vem depois e cria 16dp de respiro DENTRO da cor (≈ CSS
//padding).
fun TextA() {
    Text(
        "A", Modifier
            .background(Color.Yellow)
            .padding(16.dp)
    )

}

@Preview(showBackground = true)
@Composable
// B: padding primeiro, então ficam 16dp de espaço SEM cor em volta (≈ CSS
//margin).
// O fundo amarelo só pinta dali pra dentro e fica colado no texto.
fun TextB() {

    Text(
        "B", Modifier
            .padding(16.dp)
            .background(Color.Yellow)
    )
}

@Preview(showBackground = true)
@Composable

// Parte 2: uma cadeia só, lida de fora pra dentro:
// borda preta → 8dp sem cor (margem) → fundo amarelo → 16dp de padding interno
fun BorderMargePadding() {
    Text(
        "Borda + margem + padding", Modifier
            .border(2.dp, Color.Black)
            .padding(8.dp)
            .background(
                Color.Yellow
            )
            .padding(16.dp)
    )
}

@Composable
fun PlotarTextos(modifier: Modifier = Modifier) {
    Column(modifier, Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        TextA()
        TextB()
        BorderMargePadding()
    }
}

@Composable
fun CardPlayList(
    modifier: Modifier = Modifier,
    titulo: String,
    quantididade: Int,
    chips: List<String>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = titulo)
        Text(text = "$quantididade");
        Row(Modifier, Arrangement.spacedBy(8.dp)) {

            for (chip in chips) {
                Box(
                    Modifier
                        .size(72.dp, 32.dp)
                        .background(Color.Yellow), Alignment.Center
                ) {
                    Text(text =  chip);
                }
            }


        }

    }

}

@Preview(showBackground = true)
@Composable
fun CardPlayListPreview() {
    TemperatureConverterTheme() {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CardPlayList(
               modifier = Modifier.fillMaxSize(),
                "Rock internacional",
                35,
                mutableListOf("turnstile", "slipknot", "dazing")
            )

            CardPlayList(
                modifier = Modifier.border(width = 15.dp, color = Color.Black),
                "Rock nacional",
                90,
                mutableListOf("jotaQuest", "sepultura", "capital inicial")
            )
            PlotarTextos()
        }

    }
}

@Composable
private fun Chip(texto: String, modifier: Modifier = Modifier){
    // nao sei bem porque nao publica se o card pode ser reutilizavel neste caso. Qual a explicacao?
}

