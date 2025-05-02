package com.example.a75hard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DayScreen(day: String) {
    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${stringResource(R.string.day_title)} $day",
                style = MaterialTheme.typography.displayMedium,
            )

            Spacer(modifier = Modifier.size(40.dp))

            Text(
                text = stringResource(R.string.day_screen_diet_header)
            )

            Spacer(modifier = Modifier.size(40.dp))

            Text(
                text = stringResource(R.string.day_screen_outside_workout)
            )

            Spacer(modifier = Modifier.size(40.dp))

            Text(
                text = stringResource(R.string.day_screen_second_workout)
            )

            Spacer(modifier = Modifier.size(40.dp))

            Text(
                text = stringResource(R.string.day_screen_water_drank)
            )

            Spacer(modifier = Modifier.size(40.dp))

            Text(
                text = stringResource(R.string.day_screen_pages_read)
            )

            Spacer(modifier = Modifier.size(40.dp))

            Text(
                text = stringResource(R.string.day_screen_progress_photo)
            )

        }
    }
// 1. Follow a diet
    // Checkbox for diet followed
// 2. Complete 2 45 minute workouts, 1 must be outside
    // 2 checkboxes for workouts?
// 3. Drink 4.5l of water
    //Input field to add amount of water drank
    //Progress bar x/4.5l
// 4. Read 10 pages of a book
    //Same as for water maybe?
    //Or maybe just a checkbox for this too
// 5. Take a progress photo
    //Button to upload photo
    //Display photo once uploaded
    //Delete/Replace photo option
// Notes section
// Maybe a checkbox to mark day as complete, or maybe this should be done automatically once all checkboxes ticked,
// progress bar complete, and photo uploaded
// Possibly also add info icons
}

@Preview
@Composable
fun DayScreenPreview() {
    DayScreen("1")
}