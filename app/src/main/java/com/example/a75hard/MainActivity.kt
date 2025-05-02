package com.example.a75hard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.a75hard.navigation.NavHost
import com.example.a75hard.ui.theme._75HardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _75HardTheme {
                _75HardApp()
            }
        }
    }
}

@Composable
fun _75HardApp() {
    _75HardTheme {
        var navController = rememberNavController()
        Scaffold(
        ) { innerPadding ->
            NavHost(navController)
        }
    }
}