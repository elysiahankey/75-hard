package com.example.a75hard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a75hard.R
import com.example.a75hard.helpers.WaterPrefs

@Composable
fun WaterTracker(dayNumber: String) { // Pass in the day identifier (e.g., "Day 1")
    val context = LocalContext.current
    val waterFlow = WaterPrefs.getWaterProgress(context, dayNumber)
    var currentProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(waterFlow) {
        waterFlow.collect { progress ->
            currentProgress = progress
        }
    }

    var input by remember { mutableStateOf("") }
    val goal = 4500f

    LaunchedEffect(currentProgress) {
        WaterPrefs.saveWaterProgress(context, currentProgress, dayNumber)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            TextField(
                value = input,
                onValueChange = { input = it },
                label = { Text(text = stringResource(R.string.water_tracker_input_label)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        val inputValue = input.toFloatOrNull()
                        if (inputValue != null) {
                            // Update the current progress based on input
                            currentProgress =
                                (currentProgress + inputValue).coerceAtMost(goal) / goal
                            input = "" // Reset input after submission
                        }
                    }
                ),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            Button(
                onClick = {
                    val inputValue = input.toFloatOrNull()
                    if (inputValue != null) {
                        // Update the current progress based on input
                        currentProgress = (currentProgress + inputValue).coerceAtMost(goal) / goal
                        input = "" // Reset input after submission
                    }
                },
                shape = RectangleShape,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(text = stringResource(R.string.water_tracker_button_label))
            }
        }

        Spacer(modifier = Modifier.size(20.dp))

        LinearProgressIndicator(
            progress = { currentProgress },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.size(20.dp))

        Text(text = "${(currentProgress * 100).toInt()}% of daily goal")
    }
}

@Preview
@Composable
fun WaterTrackerPreview() {
    WaterTracker(
        dayNumber = "1"
    )
}