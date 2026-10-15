package com.adriel.temperatureconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.adriel.temperatureconverter.ui.TelaTemperatura
import com.adriel.temperatureconverter.ui.theme.TemperatureConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TemperatureConverterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TelaTemperatura(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



