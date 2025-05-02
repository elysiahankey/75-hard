package com.example.a75hard.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a75hard.ui.theme.onPrimaryContainerLight
import com.example.a75hard.ui.theme.inversePrimaryLight

@Composable
fun Grid(modifier: Modifier = Modifier, onClick: (Int) -> Unit) {
    val numbers = (1..75).toList()

    // Change backgroiund colour if day is complete

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
    ) {
        items(numbers) { number ->
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .padding(4.dp)
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(colors = listOf(onPrimaryContainerLight, inversePrimaryLight)),
                        shape = RectangleShape
                    )
                    .clickable { onClick(number) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = number.toString(), fontSize = 20.sp)
            }
        }
    }
}



@Preview
@Composable
fun GridPreview() {
    Grid(
        modifier = Modifier.padding(1.dp),
        onClick = {"1"}
    )
}