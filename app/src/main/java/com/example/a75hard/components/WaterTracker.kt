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
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun WaterTracker(dayNumber: String) {
    val context = LocalContext.current
    val goal = 4500f

    // 1. State for current progress (mutable locally)
    var currentProgress by remember { mutableStateOf<Float?>(null) }

    val coroutineScope = rememberCoroutineScope()

    // 2. Read from DataStore only once on first composition
    LaunchedEffect(dayNumber) {
        WaterPrefs.getWaterProgress(context, dayNumber).collect { savedProgress ->
            if (currentProgress == null) {
                currentProgress = savedProgress
            }
        }
    }

    var input by remember { mutableStateOf("") }

    // Only render UI when the progress is loaded
    currentProgress?.let { progress ->

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
                                val updated = (progress * goal + inputValue).coerceAtMost(goal)
                                currentProgress = updated / goal
                                coroutineScope.launch {
                                    WaterPrefs.saveWaterProgress(context, updated / goal, dayNumber)
                                }
                                input = ""
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
                            val updated = (progress * goal + inputValue).coerceAtMost(goal)
                            currentProgress = updated / goal
                            coroutineScope.launch {
                                WaterPrefs.saveWaterProgress(context, updated / goal, dayNumber)
                            }
                            input = ""
                        }
                    },
                    shape = RectangleShape,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(text = stringResource(R.string.water_tracker_button_label))
                }
            }

            Spacer(modifier = Modifier.size(20.dp))

            LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.size(20.dp))

            Text(text = "${(progress * 100).toInt()}% of daily goal")
        }
    }
}


@Composable
fun LinearDeterminateIndicator(currentProgress: Float) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        LinearProgressIndicator(
            progress = { currentProgress },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
fun WaterTrackerPreview() {
    WaterTracker(
        dayNumber = "1"
    )
}