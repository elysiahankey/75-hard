package com.example.a75hard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.a75hard.R
import com.example.a75hard.viewmodels.ViewModel

@Composable
fun WaterTracker(viewModel: ViewModel = hiltViewModel()) {
    val goal = ViewModel.WATER_GOAL_ML
    val currentProgress by viewModel.waterDrank.collectAsState()
    var input by remember { mutableStateOf("") }
    var showWater by remember { mutableStateOf(false) }

    currentProgress.let { progress ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .clickable { showWater = !showWater }
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.day_screen_water_drank_title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (showWater) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Tap to expand"
                )
            }

            if (showWater) {
                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(12.dp)
                            )
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
                                trailingIcon = {
                                    Text(
                                        text = "ml",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )

                            Button(
                                onClick = {
                                    val inputValue = input.toFloatOrNull()
                                    if (inputValue != null) {
                                        viewModel.addWater(inputValue.toInt())
                                        input = ""
                                    }
                                },
                                shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Text(text = stringResource(R.string.water_tracker_button_label))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.size(20.dp))

                    LinearProgressIndicator(
                        progress = { currentProgress.toFloat() / goal },
                        modifier = Modifier.fillMaxWidth(),
                        drawStopIndicator = {}
                    )

                    Spacer(modifier = Modifier.size(20.dp))

                    Text(text = "${currentProgress}ml / ${goal}ml")

                    TextButton(
                        onClick = {
                            viewModel.resetWater()
                        },
                        enabled = progress > 0f,
                        modifier = Modifier.padding(top = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.water_tracker_reset_button_label)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WaterTrackerPreview() {
    WaterTracker()
}