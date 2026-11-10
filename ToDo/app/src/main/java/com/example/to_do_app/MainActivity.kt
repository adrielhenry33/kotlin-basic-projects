package com.example.to_do_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.to_do_app.ui.todo.TelaTarefas
import com.example.to_do_app.ui.theme.TodoappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   TelaTarefas(modifier = Modifier.padding(paddingValues = innerPadding))
                }
            }
        }
    }
}

