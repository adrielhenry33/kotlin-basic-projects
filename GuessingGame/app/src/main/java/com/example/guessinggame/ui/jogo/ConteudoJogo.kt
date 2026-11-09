package com.example.guessinggame.ui.jogo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
private fun ConteudoJogoPreview() {
    ConteudoJogo(
        uiState = JogoUiState(
            result = GuessResult.TOO_LOW,
            guessesList = listOf(80, 30),
            typedText = ""
        ),
        onTypedTextChange = {},
        guess = {},
        resetGame = {},
        range = 1..100
    )
}


@Composable
fun ConteudoJogo(
    uiState: JogoUiState,
    onTypedTextChange: (String) -> Unit,
    guess: () -> Unit,
    resetGame: () -> Unit,
    range: IntRange,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(top = 80.dp, start = 30.dp, end = 30.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Adivinhe o número", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Text(
            "Pensei num número de ${range.first} a ${range.last}",
            fontSize = 20.sp
        )

        CampoNumero(
            uiState.typedText,
            onValueChange = { onTypedTextChange(it) },
            Modifier.fillMaxWidth(),
            range
        )

        Button(onClick = {
            guess()
        }, Modifier.fillMaxWidth()) {
            Text("Chutar")
        }

        if (uiState.result != null) {
            Card(
                Modifier
                    .height(100.dp)
                    .fillMaxWidth(),

                ) {
                Column(
                    Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val mensagem = when (uiState.result) {
                        GuessResult.TOO_HIGH ->
                            "Muito alto! ${uiState.guessesList.last()} é maior  do que o numero "

                        GuessResult.TOO_LOW ->
                            "Muito baixo! ${uiState.guessesList.last()} é menor  do que o numero "

                        GuessResult.SUCCESS ->
                            "Sucesso! voce acertou o numero,  ele é ${uiState.guessesList.last()} "
                    }
                    Text(
                        text = mensagem,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Text("Ultimos palpites ${uiState.guessesList.joinToString(" - ")}")

            if (uiState.result == GuessResult.SUCCESS) Text("Acertou em ${uiState.guessesList.size} tentativas");

            HorizontalDivider(Modifier.fillMaxWidth())

            Button(
                onClick = { resetGame() },
                Modifier.fillMaxWidth()
            ) { Text("Novo Jogo") }
        }

    }
}