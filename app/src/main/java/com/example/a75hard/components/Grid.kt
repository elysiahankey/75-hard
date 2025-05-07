package com.example.a75hard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Grid(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
    completedDays: Set<String>) {

    val numbers = (1..75).toList()

    Log.d("Grid", "Completed days: $completedDays")

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
    ) {
        items(numbers) { number ->
            val isComplete = completedDays.contains(number.toString())
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .padding(4.dp)
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.onPrimaryContainer,
                                MaterialTheme.colorScheme.inversePrimary)
                        ),
                        shape = RectangleShape
                    )
                    .clickable { onClick(number) }
                    .background(
                        color = if (isComplete)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    fontSize = 20.sp,
                    color = if (isComplete)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}