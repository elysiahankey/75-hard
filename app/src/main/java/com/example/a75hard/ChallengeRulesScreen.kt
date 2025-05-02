package com.example.a75hard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.a75hard.navigation.BottomNavBar

@Composable
fun ChallengeRulesScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.challenge_rules_header),
                    style = MaterialTheme.typography.displayLarge,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.size(20.dp))

            val rules = listOf<Int>(
                R.string.rule_1,
                R.string.rule_2,
                R.string.rule_3,
                R.string.rule_4,
                R.string.rule_5
            )

            rules.forEachIndexed { index, resId ->
                Text(
                    text = "${index + 1}. ${stringResource(id = resId)}",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.size(20.dp))
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