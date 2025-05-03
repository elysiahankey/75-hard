package com.example.a75hard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.a75hard.R
import com.example.a75hard.helpers.WeightHelper
import com.example.a75hard.viewmodels.DayViewModel
import kotlinx.coroutines.launch

@Composable
fun WeightTracker(dayNumber: String, viewModel: DayViewModel = hiltViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var input by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        WeightHelper.getWeightState(context, dayNumber).collect { savedWeight ->
            input = savedWeight
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.day_screen_weight_tracker_title),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = input,
            onValueChange = {
                input = it
                coroutineScope.launch {
                    WeightHelper.saveWeightState(context, dayNumber, it) // Pass the new input value
                }
            },
            label = { Text(text = stringResource(R.string.weight_tracker_weight_field)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {}
            ),
            trailingIcon = {
                Text(
                    text = "kg",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            singleLine = true,
            modifier = Modifier.clip(RoundedCornerShape(12.dp)).fillMaxWidth()
        )
    }
}