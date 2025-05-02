package com.example.a75hard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.a75hard.components.WaterTracker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayScreen(navController: NavHostController, day: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Day $day") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(100.dp))

            Text(
                text = stringResource(R.string.day_screen_diet_header)
            )
            // 1. Follow a diet
            // Checkbox for diet followed

            Spacer(modifier = Modifier.size(40.dp))

            Text(
                text = stringResource(R.string.day_screen_outside_workout)
            )

            Spacer(modifier = Modifier.size(40.dp))

            Text(
                text = stringResource(R.string.day_screen_second_workout)
            )
            // 2. Complete 2 45 minute workouts, 1 must be outside
            // 2 checkboxes for workouts?

            Spacer(modifier = Modifier.size(40.dp))

            Text(
                text = stringResource(R.string.day_screen_water_drank)
            )
            WaterTracker(dayNumber = day)
            // 3. Drink 4.5l of water
            //Input field to add amount of water drank
            //Progress bar x/4.5l

            Spacer(modifier = Modifier.size(40.dp))

            Text(
                text = stringResource(R.string.day_screen_pages_read)
            )
            // 4. Read 10 pages of a book
            //Same as for water maybe?
            //Or maybe just a checkbox for this too

            Spacer(modifier = Modifier.size(40.dp))

            Text(
                text = stringResource(R.string.day_screen_progress_photo)
            )
            // 5. Take a progress photo
            //Button to upload photo
            //Display photo once uploaded
            //Delete/Replace photo option

            // Notes section
            // Maybe a checkbox to mark day as complete, or maybe this should be done automatically once all checkboxes ticked,
            // progress bar complete, and photo uploaded
            // Possibly also add info icons

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