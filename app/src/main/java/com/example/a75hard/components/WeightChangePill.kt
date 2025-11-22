package com.example.a75hard.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.a75hard.R
import com.example.a75hard.helpers.WeightHelper.getWeightChange
import kotlin.math.abs

@Composable
fun WeightChangePill(context: Context) {

    val weightChange by produceState(initialValue = 0.0, context) {
        value = getWeightChange(context = context)
    }
    val roundedWeightChange = "%.1f".format(weightChange).toDouble()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter =
                if (weightChange < 0) painterResource(R.drawable.up_arrow)
                else if (weightChange > 0) painterResource(R.drawable.down_arrow)
                else painterResource(R.drawable.no_change),
            tint = MaterialTheme.colorScheme.onSecondary,
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = "${abs(roundedWeightChange)}kg",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center,
        )
    }
}