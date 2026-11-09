package com.example.guessinggame.ui.jogo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class GuessResult { SUCCESS, TOO_HIGH, TOO_LOW }

data class JogoUiState(
    val result: GuessResult? = null,
    val guessesList: List<Int> = emptyList(),
    val typedText: String = ""
)

class JogoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(JogoUiState())
    val uiState: StateFlow<JogoUiState> = _uiState.asStateFlow()


     val range = 1..100
    private var randomNumber = range.random()

    fun onTypedTextChange(text: String) {
        _uiState.update { it.copy(typedText = text) }
    }

    fun guess() {
        val numberGuess = _uiState.value.typedText.trim().toIntOrNull() ?: return
        if (numberGuess !in range || _uiState.value.result == GuessResult.SUCCESS
        ) return

        _uiState.update {
            it.copy(
                guessesList = it.guessesList + numberGuess,
                result = when {
                    numberGuess > randomNumber -> GuessResult.TOO_HIGH
                    numberGuess < randomNumber -> GuessResult.TOO_LOW
                    else -> GuessResult.SUCCESS
                },
                typedText = ""
            )
        }
    }

    fun resetGame() {
        _uiState.update {
            JogoUiState()
        }
        randomNumber = range.random()
    }


}