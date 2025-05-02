package com.example.a75hard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.a75hard.components.Checkboxes
import com.example.a75hard.components.Notes
import com.example.a75hard.components.ProgressPhoto
import com.example.a75hard.components.WaterTracker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayScreen(
    navController: NavHostController,
    dayNumber: String,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val dayViewModel: DayViewModel = hiltViewModel()
    dayViewModel.bindHomeViewModel(homeViewModel)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Day $dayNumber") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState),
        ) {

            val checkboxList: List<Int> = listOf(
                R.string.day_screen_diet_header,
                R.string.day_screen_outside_workout,
                R.string.day_screen_second_workout,
                R.string.day_screen_pages_read
            )

            Checkboxes(
                items = checkboxList,
                dayNumber = dayNumber
            )

            WaterTracker(dayNumber = dayNumber)

            ProgressPhoto(dayNumber = dayNumber)

            Notes(dayNumber = dayNumber)

            // Possibly also add info icons

            // Add a weight option - maybe add a starting weight field to a settings screen or something
            // An emoji option in the top bar? that also displays in the grid?

            // Step trackers from Google Health

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