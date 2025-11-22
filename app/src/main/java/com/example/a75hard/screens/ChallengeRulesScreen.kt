package com.example.a75hard.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeRulesScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.challenge_rules_title),
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

        val scrollState = rememberScrollState()

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
                    .padding(
                        top = 35.dp,
                        start = 20.dp,
                        end = 20.dp
                    )
                    .verticalScroll(scrollState)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.onPrimary),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 20.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val rules = listOf<Int>(
                        R.string.rule_1,
                        R.string.rule_2,
                        R.string.rule_3,
                        R.string.rule_4,
                        R.string.rule_5,
                        R.string.rule_6
                    )

                    Spacer(modifier = Modifier.size(15.dp))

                    rules.forEachIndexed { index, resId ->
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${index + 1}.",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.1f)
                            )
                            Text(
                                text = stringResource(id = resId),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(0.9f)
                            )
                        }
                        Spacer(modifier = Modifier.size(15.dp))
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))

                Box(
                    modifier = Modifier.padding(
                        vertical = 20.dp,
                        horizontal = 16.dp
                    )
                ) {
                    Text(
                        text = stringResource(R.string.challenge_fail_text),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
            }
        }
    }
}

@Preview
@Composable
fun ChallengeRulesScreenPreview() {
    var navController = rememberNavController()
    ChallengeRulesScreen(navController)
}