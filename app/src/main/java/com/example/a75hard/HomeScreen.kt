package com.example.a75hard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.a75hard.components.Grid
import com.example.a75hard.navigation.BottomNavBar

@Composable
fun HomeScreen(navController: NavHostController, onClickDay: (String) -> Unit) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.home_screen_title),
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(40.dp))

            Grid(
                modifier = Modifier.padding(innerPadding),
                onClick = { dayNumber ->
                    onClickDay(dayNumber.toString())
                }
            )
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    var navController = rememberNavController()
    HomeScreen(
        navController = navController,
        onClickDay = {}
    )
}