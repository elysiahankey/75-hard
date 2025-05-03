package com.example.a75hard.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.a75hard.viewmodels.DayViewModel
import com.example.a75hard.R
import com.example.a75hard.helpers.CheckboxHelper
import com.example.a75hard.helpers.CheckboxHelper.getCheckboxState
import kotlinx.coroutines.launch

@Composable
fun Checkboxes(items: List<Int>, dayNumber: String, viewModel: DayViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val checkboxStates = items.map { item ->
        item to getCheckboxState(context, dayNumber, item).collectAsState(initial = false).value
    }

    val allChecked = checkboxStates.all { it.second }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        checkboxStates.forEach { (item, isChecked) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { newChecked ->
                        coroutineScope.launch {
                            CheckboxHelper.saveCheckboxState(context, dayNumber, item, newChecked)
                        }
                    }
                )
                Text(text = stringResource(item))
            }
        }
    }

    if (allChecked) {
        viewModel.setChecked(value = true)
    } else {
        viewModel.setChecked(value = false)
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