package com.example.a75hard.screens

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.a75hard.R
import com.example.a75hard.helpers.WaterHelper.getAllWater
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterTrackerScreen(
    navController: NavHostController
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.water_tracker_title),
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

                val waterEntries by produceState(initialValue = emptyList<Pair<Int, Int>>(), context) {
                    value = getAllWater(context)
                }

                val waterStrings = waterEntries.map { it.second.toDouble() }
                val minWater = (waterStrings.minOrNull()?.minus(100)) ?: 0.0
                val maxWater = (waterStrings.maxOrNull()?.plus(100)) ?: 0.0

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
                                    values = waterStrings,
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
                        minValue = minWater,
                        maxValue = maxWater,
                        labelHelperProperties = LabelHelperProperties(
                            enabled = false,
                        )
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Row {
                        Text("", modifier = Modifier.weight(1f))
                        Text("Water intake", modifier = Modifier.weight(1f))
                    }

                    HorizontalDivider()

                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        waterEntries.forEach { (day, water) ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text("Day $day", modifier = Modifier.weight(1f))
                                Text("%,dml".format(water.toInt()), modifier = Modifier.weight(1f))
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
fun WaterTrackerScreenPreview() {
    val navController = rememberNavController()
    WaterTrackerScreen(
        navController = navController
    )
}