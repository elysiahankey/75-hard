package com.example.a75hard.screens

import com.airbnb.lottie.compose.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.a75hard.R
import com.example.a75hard.components.Checkboxes
import com.example.a75hard.components.Notes
import com.example.a75hard.components.ProgressPhoto
import com.example.a75hard.components.WaterTracker
import com.example.a75hard.components.WeightTracker
import com.example.a75hard.viewmodels.DayViewModel
import com.example.a75hard.viewmodels.HomeViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayScreen(
    navController: NavHostController,
    dayNumber: String,
    homeViewModel: HomeViewModel = hiltViewModel(),
    dayViewModel: DayViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        dayViewModel.bindHomeViewModel(homeViewModel)
    }

    val recentlyCompletedDay by homeViewModel.recentlyCompletedDay.collectAsState()
    var showSuccessAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(recentlyCompletedDay) {
        if (recentlyCompletedDay == dayNumber) {
            showSuccessAnimation = true
            delay(3700)
            showSuccessAnimation = false
            homeViewModel.clearRecentlyCompletedDay() // Reset trigger
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Day $dayNumber",
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->

        val scrollState = rememberScrollState()

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
                contentScale = ContentScale.Crop
            )

            if (showSuccessAnimation) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success_lottie))
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = 5
                )

                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1f)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.onPrimary),
                ) {
                    Checkboxes(
                        items = DayViewModel.Companion.checkboxList,
                        dayNumber = dayNumber
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))

                    WaterTracker(dayNumber = dayNumber)

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))

                    ProgressPhoto(dayNumber = dayNumber)

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))

                    WeightTracker(dayNumber = dayNumber)

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))

                    Notes(dayNumber = dayNumber)

                    // Possibly also add info icons

                    // Add a weight option - maybe add a starting weight field to a settings screen or something
                    // An emoji option in the top bar? that also displays in the grid?

                    // Step trackers from Google Health

                    Button(
                        onClick = { dayViewModel.resetDay() },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.day_screen_reset_day_button)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DayScreenPreview() {
    var navController = rememberNavController()

    DayScreen(
        navController = navController,
        "1"
    )
}