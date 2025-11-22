package com.example.a75hard.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.a75hard.BuildConfig
import com.example.a75hard.R
import com.example.a75hard.components.SettingsButton
import com.example.a75hard.navigation.BottomNavBar
import com.example.a75hard.viewmodels.ViewModel

@Composable
fun SettingsScreen(
    navController: NavHostController,
    onClickWeightTracker: () -> Unit,
    onClickWaterTracker: () -> Unit,
    onClickRules: () -> Unit,
    onClickAbout: () -> Unit,
    onClickDebugScreen: () -> Unit,
    viewModel: ViewModel = hiltViewModel()
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        val scrollState = rememberScrollState()
        var showResetDialog by remember { mutableStateOf(false) }

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
                    .verticalScroll(scrollState)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.onPrimary),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                ) {
                    Text(
                        text = stringResource(R.string.settings_title),
                        style = MaterialTheme.typography.displayLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    SettingsButton(
                        onClick = { onClickRules() },
                        label = stringResource(R.string.challenge_rules_title)
                    )
                    HorizontalDivider()
                    SettingsButton(
                        onClick = { onClickWeightTracker() },
                        label = stringResource(R.string.settings_weight_tracker_button)
                    )
                    HorizontalDivider()
                    SettingsButton(
                        onClick = { onClickWaterTracker() },
                        label = stringResource(R.string.settings_water_tracker_button)
                    )
                    HorizontalDivider()
                    SettingsButton(
                        onClick = { showResetDialog = true },
                        label = stringResource(R.string.settings_reset_all_button)
                    )
                    if (showResetDialog) {
                        AlertDialog(
                            onDismissRequest = { showResetDialog = false },
                            title = { Text(stringResource(R.string.reset_app_dialog_title)) },
                            text = { Text(stringResource(R.string.reset_app_dialog_text)) },
                            confirmButton = {
                                TextButton(onClick = {
                                    viewModel.resetAllDays()
                                    showResetDialog = false
                                }) {
                                    Text(stringResource(R.string.reset_app_dialog_confirm))
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showResetDialog = false }) {
                                    Text(stringResource(R.string.reset_app_dialog_cancel))
                                }
                            }
                        )
                    }
                    HorizontalDivider()
                    SettingsButton(
                        onClick = { onClickAbout() },
                        label = stringResource(R.string.settings_libraries_button)
                    )
                    if (BuildConfig.DEBUG) {
                        HorizontalDivider()
                        SettingsButton(
                            onClick = { onClickDebugScreen() },
                            label = "Debug"
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                ) {
                    Text(
                        text = "Version: ${BuildConfig.VERSION_CODE} (${BuildConfig.VERSION_NAME})",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    var navController = rememberNavController()
    SettingsScreen(
        navController = navController,
        onClickWeightTracker = {},
        onClickWaterTracker = {},
        onClickRules = {},
        onClickAbout = {},
        onClickDebugScreen = {}
    )
}