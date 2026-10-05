package com.adriel.temperatureconverter.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
fun TextA(){
    Text("A", Modifier.background(Color.Yellow).padding(16.dp))

}

@Preview(showBackground = true)
@Composable
// B: padding primeiro, então ficam 16dp de espaço SEM cor em volta (≈ CSS
//margin).
// O fundo amarelo só pinta dali pra dentro e fica colado no texto.
fun TextB(){

    Text("B", Modifier.padding(16.dp).background(Color.Yellow))
}

@Preview(showBackground = true)
@Composable

// Parte 2: uma cadeia só, lida de fora pra dentro:
// borda preta → 8dp sem cor (margem) → fundo amarelo → 16dp de padding interno
fun BodarMargePadding(){
    Text("Borda + margem + padding", Modifier.border(2.dp, Color.Black).padding(8.dp).background(
        Color.Yellow).padding(16.dp))
}

@Composable
fun CardPlayList(modifier: Modifier = Modifier){
    Column(modifier.padding(16.dp),  Arrangement.spacedBy(8.dp)) {
        Text("Rock Nacional")
        Text("42 Musicas")
        Row(Modifier, Arrangement.spacedBy(8.dp) ) {
            Box(Modifier.size(72.dp, 32.dp).background(Color.Yellow), Alignment.Center){
                Text("Anos 80")
            }


            Box(Modifier.size(72.dp, 32.dp).background(Color.Yellow), Alignment.Center){
                Text("Anos 90")
            }

            Box(Modifier.size(72.dp, 32.dp).background(Color.Yellow), Alignment.Center){
                Text("Ao vivo")
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun CardPlayListPreview(){
    TemperatureConverterTheme() {
        CardPlayList()
    }
}

