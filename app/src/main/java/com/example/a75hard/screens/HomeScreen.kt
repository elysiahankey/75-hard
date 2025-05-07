package com.example.a75hard.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.a75hard.R
import com.example.a75hard.components.Grid
import com.example.a75hard.navigation.BottomNavBar
import com.example.a75hard.viewmodels.ViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    onClickDay: (String) -> Unit,
    viewModel: ViewModel = hiltViewModel()
) {
    val completedDays by viewModel.completedDays.collectAsState()
    val completedCount = completedDays.count()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(R.drawable.plant_background),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(-1f),
                contentScale = ContentScale.FillHeight
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.onPrimary),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                ) {
                    Text(
                        text = stringResource(R.string.home_title),
                        style = MaterialTheme.typography.displayLarge,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.size(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 20.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(12.dp)
                        )
                    ,
                ) {
                    Log.d("HomeScreen", "Count is ${completedCount}")
                    Text(
                        text = "${completedCount}/75 days complete",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.size(20.dp))

                Grid(
                    onClick = { dayNumber -> onClickDay(dayNumber.toString()) },
                    completedDays = completedDays
                )
            }
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