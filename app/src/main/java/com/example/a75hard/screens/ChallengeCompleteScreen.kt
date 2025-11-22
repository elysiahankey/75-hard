package com.example.a75hard.screens

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.a75hard.R
import com.example.a75hard.components.WeightChart
import com.example.a75hard.helpers.ProgressPhotoHelper
import com.example.a75hard.helpers.WaterHelper.getTotalWaterInL
import com.example.a75hard.helpers.WeightHelper.getAllWeights
import com.example.a75hard.helpers.WeightHelper.getWeightChange
import kotlin.math.abs

private enum class ChallengeStep {
    INTRO, WEIGHT, PHOTOS, WATER, FINISHED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeCompleteScreen(
    navController: NavController
) {

    Scaffold { innerPadding ->

        val scrollState = rememberScrollState()
        var currentStep by remember { mutableStateOf(ChallengeStep.INTRO) }
        var isNextButtonVisible by remember { mutableStateOf(false) }
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.verticalScroll(scrollState),
                ) {

                    AnimatedContent(
                        targetState = currentStep,
                        label = "Challenge Step Animation",
                        transitionSpec = {
                            slideInHorizontally { width -> width } + fadeIn() togetherWith
                                    slideOutHorizontally { width -> -width } + fadeOut()
                        }
                    ) { step ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            when (step) {
                                ChallengeStep.INTRO -> CompletedIntro { isNextButtonVisible = true }
                                ChallengeStep.WEIGHT -> CompletedWeight(context) { isNextButtonVisible = true }
                                ChallengeStep.PHOTOS -> {
                                    CompletedPhotoProgress(context) {
                                        isNextButtonVisible = true
                                    }
                                }
                                ChallengeStep.WATER -> CompletedWater(context) { isNextButtonVisible = true }
                                ChallengeStep.FINISHED -> CompletedFinish { isNextButtonVisible = true }
                            }

                            Spacer(modifier = Modifier.height(40.dp))

                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = isNextButtonVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Button(
                        onClick = {
                            if (currentStep == ChallengeStep.FINISHED) {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            } else {
                                isNextButtonVisible = false

                                val nextStep = when (currentStep) {
                                    ChallengeStep.INTRO -> ChallengeStep.WEIGHT
                                    ChallengeStep.WEIGHT -> ChallengeStep.PHOTOS
                                    ChallengeStep.PHOTOS -> ChallengeStep.WATER
                                    ChallengeStep.WATER -> ChallengeStep.FINISHED
                                    else -> ChallengeStep.FINISHED
                                }

                                currentStep = nextStep
                            }
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(text = if (currentStep == ChallengeStep.FINISHED) "Finish" else "Next")
                    }
                }
            }
        }

    }
}

@Composable
fun CompletedIntro(onAnimationFinished: () -> Unit) {
    var showTitle by remember { mutableStateOf(false) }
    var showText1 by remember { mutableStateOf(false) }
    var showText2 by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(300)
        showTitle = true
        kotlinx.coroutines.delay(1000)
        showText1 = true
        kotlinx.coroutines.delay(1000)
        showText2 = true
        kotlinx.coroutines.delay(1000)
        onAnimationFinished()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedVisibility(visible = showTitle, enter = fadeIn() + expandVertically()) {
            Text(
                text = stringResource(R.string.challenge_complete_title),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )
        }

        AnimatedVisibility(visible = showText1, enter = fadeIn() + expandVertically()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.challenge_complete_text_1),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }

        AnimatedVisibility(visible = showText2, enter = fadeIn() + expandVertically()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.challenge_complete_text_2),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun CompletedWeight(context: Context, onAnimationFinished: () -> Unit) {
    var showTitle by remember { mutableStateOf(false) }
    var showChart by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(300)
        showTitle = true
        kotlinx.coroutines.delay(1000)
        showChart = true
        kotlinx.coroutines.delay(2500)
        showText = true
        kotlinx.coroutines.delay(1000)
        onAnimationFinished()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedVisibility(visible = showTitle, enter = fadeIn() + expandVertically()) {
            Text(
                text = stringResource(R.string.challenge_complete_weight_text),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
        }

        val weightEntries by produceState(
            initialValue = emptyList(),
            context
        ) {
            value = getAllWeights(context)
        }
        val weightChange by produceState(initialValue = 0.0, context) {
            value = getWeightChange(context = context)
        }
        val roundedWeightChange = "%.1f".format(weightChange).toDouble()

        AnimatedVisibility(visible = showChart, enter = fadeIn() + expandVertically()) {
            WeightChart(weightEntries = weightEntries)
        }

        AnimatedVisibility(visible = showText, enter = fadeIn() + expandVertically()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val weightChangeText =
                    if (weightChange < 0) "You've gained ${abs(roundedWeightChange)}kg!"
                    else if (weightChange > 0) "You've lost ${abs(roundedWeightChange)}kg!"
                    else "Your weight has stayed stable!"
                Text(
                    text = weightChangeText,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun CompletedPhotoProgress(
    context: Context,
    onAnimationFinished: () -> Unit
) {

    var showDay1 by remember { mutableStateOf(false) }
    var showDay75 by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(300)
        showDay1 = true
        kotlinx.coroutines.delay(1500)
        showDay75 = true
        kotlinx.coroutines.delay(1000)
        onAnimationFinished()
    }

    val day1Photo by ProgressPhotoHelper.getPhotoState(context, "1")
        .collectAsState(initial = "")

    val day75Photo by ProgressPhotoHelper.getPhotoState(context, "75")
        .collectAsState(initial = "")

    if (day1Photo.isNotBlank() && day75Photo.isNotBlank()) {
        AnimatedVisibility(
            visible = showDay1,
            enter = fadeIn() + expandVertically()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                val day1Bitmap = BitmapFactory.decodeFile(day1Photo)
                if (day1Bitmap != null) {
                    Text(
                        text = "Here's where you started:",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Image(
                        bitmap = day1Bitmap.asImageBitmap(),
                        contentDescription = "Day 1 progress photo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    SideEffect {
                        onAnimationFinished()
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(
            visible = showDay75,
            enter = fadeIn() + expandVertically()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val day75Bitmap = BitmapFactory.decodeFile(day75Photo)
                if (day75Bitmap != null) {
                    Text(
                        text = "And here's where you are now:",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Image(
                        bitmap = day75Bitmap.asImageBitmap(),
                        contentDescription = "Day 75 progress photo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Composable
fun CompletedWater(context: Context, onAnimationFinished: () -> Unit) {
    var showText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(300)
        showText = true
        kotlinx.coroutines.delay(1000)
        onAnimationFinished()
    }

    AnimatedVisibility(visible = showText, enter = fadeIn() + expandVertically()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val totalWaterDrank by produceState(initialValue = "", context) {
                value = getTotalWaterInL(context)
            }

            val totalWaterDrankText = "You drank $totalWaterDrank litres of water!"
            Text(
                text = totalWaterDrankText,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun CompletedFinish(onAnimationFinished: () -> Unit) {
    var showText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(300)
        showText = true
        kotlinx.coroutines.delay(1000)
        onAnimationFinished()
    }

    AnimatedVisibility(visible = showText, enter = fadeIn() + expandVertically()) {
        Text(
            "Challenge Complete!",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun ChallengeCompleteScreenPreview() {
    val navController = rememberNavController()
    ChallengeCompleteScreen(navController = navController)
}
