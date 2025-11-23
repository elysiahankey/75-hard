package com.example.a75hard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a75hard.R
import com.example.a75hard.helpers.TodaysBookHelper.getOrInitBookState
import com.example.a75hard.helpers.TodaysBookHelper.saveBookState
import kotlinx.coroutines.launch

@Composable
fun TodaysBook(dayNumber: String) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var inputText by remember { mutableStateOf("") }

    LaunchedEffect(dayNumber) {
        val book = getOrInitBookState(context, dayNumber)
        inputText = book
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.day_screen_todays_book),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            TextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                    coroutineScope.launch {
                        saveBookState(
                            context = context,
                            dayNumber = dayNumber,
                            bookText = it
                        )
                    }
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "clear text",
                        modifier = Modifier
                            .clickable {
                                inputText = ""
                                coroutineScope.launch {
                                    saveBookState(
                                        context = context,
                                        dayNumber = dayNumber,
                                        bookText = "<cleared>"
                                    )
                                }
                            }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodaysBookPreview() {
    TodaysBook(dayNumber = "1")
}