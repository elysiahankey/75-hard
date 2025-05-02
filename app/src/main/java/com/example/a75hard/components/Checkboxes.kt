package com.example.a75hard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a75hard.R
import com.example.a75hard.helpers.CheckboxHelper
import com.example.a75hard.helpers.CheckboxHelper.getCheckboxState
import kotlinx.coroutines.launch

@Composable
fun Checkboxes(items: List<Int>, dayNumber: String) {
    val context = LocalContext.current
    val coroutineScope =
        rememberCoroutineScope() // Get the coroutine scope for launching coroutines

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items.forEach { item ->
            // Collect the checkbox state as a Flow and use collectAsState to get its current value
            val isCheckedFlow = remember { getCheckboxState(context, dayNumber, item) }
            val isChecked =
                isCheckedFlow.collectAsState(initial = false).value // Collect state directly

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked ->
                        // Launch the coroutine to save the checkbox state
                        coroutineScope.launch {
                            CheckboxHelper.saveCheckboxState(context, dayNumber, item, isChecked)
                        }
                    }
                )
                Text(text = stringResource(item))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckboxesPreview() {
    Checkboxes(
        items = listOf(
            R.string.day_screen_diet_header,
            R.string.day_screen_outside_workout,
            R.string.day_screen_second_workout,
            R.string.day_screen_pages_read
        ),
        dayNumber = "1"
    )
}