package com.example.guessinggame.ui.jogo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TelaJogo(
    modifier: Modifier = Modifier,
    jogoViewModel: JogoViewModel = viewModel()
) {
    val state by jogoViewModel.uiState.collectAsStateWithLifecycle()
    ConteudoJogo(
        state,
        onTypedTextChange = jogoViewModel::onTypedTextChange,
        guess = jogoViewModel::guess,
        resetGame = jogoViewModel::resetGame,
        range = jogoViewModel.range,
        modifier = modifier
    )
}