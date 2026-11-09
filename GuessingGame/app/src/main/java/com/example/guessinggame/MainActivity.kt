package com.example.guessinggame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.guessinggame.ui.jogo.ConteudoJogo
import com.example.guessinggame.ui.jogo.GuessResult
import com.example.guessinggame.ui.jogo.JogoUiState
import com.example.guessinggame.ui.jogo.TelaJogo
import com.example.guessinggame.ui.theme.GuessingGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuessingGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TelaJogo(modifier = Modifier.padding(paddingValues = innerPadding))
                }
            }
        }
    }


}






