package com.example.a75hard.components

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line

@Composable
fun WeightChart(weightEntries: List<Pair<Int, String>>) {

    val weightStrings = weightEntries.map { it.second.toDouble() }
    val minWeight = (weightStrings.minOrNull()?.minus(2)) ?: 0.0
    val maxWeight = (weightStrings.maxOrNull()?.plus(2)) ?: 0.0

    Box(
        modifier = Modifier
            .padding(20.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(20.dp),
            data =
                listOf(
                    Line(
                        label = "",
                        values = weightStrings,
                        color = SolidColor(MaterialTheme.colorScheme.onPrimary),
                        firstGradientFillColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = .5f),
                        secondGradientFillColor = Color.Transparent,
                        strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                        gradientAnimationDelay = 1000,
                        drawStyle = DrawStyle.Stroke(width = 2.dp),
                    )
                ),
            animationMode = AnimationMode.Together(delayBuilder = {
                it * 500L
            }),
            minValue = minWeight,
            maxValue = maxWeight,
            labelHelperProperties = LabelHelperProperties(
                enabled = false,
            )
        )
    }
}