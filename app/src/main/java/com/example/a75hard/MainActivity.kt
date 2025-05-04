package com.example.a75hard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.a75hard.navigation.NavHost
import com.example.a75hard.ui.theme._75HardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cacheDir.listFiles { file -> file.extension == "jar" }
            ?.forEach { it.setReadOnly() }
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
            NavHost(navController)
    }
}