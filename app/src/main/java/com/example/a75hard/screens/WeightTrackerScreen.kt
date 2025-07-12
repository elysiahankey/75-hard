package com.example.a75hard.screens

import android.util.Log
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.a75hard.R
import com.example.a75hard.helpers.WeightHelper.getAllWeights
import com.example.a75hard.helpers.WeightHelper.getWeightChange
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightTrackerScreen(
    navController: NavHostController
) {

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.weight_tracker_title),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(R.drawable.plant_background),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(-1f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.onPrimary),
            ) {
                val context = LocalContext.current

                val weightEntries by produceState(initialValue = emptyList<Pair<Int, String>>(), context) {
                    value = getAllWeights(context)
                }
                val weightStrings = weightEntries.map { it.second.toDouble() }
                val minWeight = (weightStrings.minOrNull()?.minus(2)) ?: 0.0
                val maxWeight = (weightStrings.maxOrNull()?.plus(2)) ?: 0.0

                val weightChange by produceState(initialValue = 0.0, context) {
                    value = getWeightChange(context = context)
                }
                val roundedWeightChange = "%.1f".format(weightChange).toDouble()

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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(12.dp)
                            )
                        ,
                    ) {
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
                }

                Column(modifier = Modifier.padding(16.dp)) {

                    Row {
                        Text("", modifier = Modifier.weight(1f))
                        Text("Weight", modifier = Modifier.weight(1f))
                    }

                    HorizontalDivider()

                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        weightEntries.forEach { (day, weight) ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text("Day $day", modifier = Modifier.weight(1f))
                                Text("${weight}kg", modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun WeightTrackerScreenPreview() {
    var navController = rememberNavController()
    WeightTrackerScreen(navController)
}