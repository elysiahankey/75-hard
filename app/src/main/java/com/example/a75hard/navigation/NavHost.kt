package com.example.a75hard.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.a75hard.screens.ChallengeCompleteScreen
import com.example.a75hard.screens.LibrariesScreen
import com.example.a75hard.screens.ChallengeRulesScreen
import com.example.a75hard.screens.DayScreen
import com.example.a75hard.screens.HomeScreen
import com.example.a75hard.screens.SettingsScreen
import com.example.a75hard.screens.WaterTrackerScreen
import com.example.a75hard.screens.WeightTrackerScreen

fun NavHostController.navigateTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

@Composable
fun NavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
    ) {
        composable(route = Home.route) {
            HomeScreen(
                navController = navController,
                onClickDay = { dayNumber ->
                    navController.navigate(DayScreen.createRoute(dayNumber))
                }
            )
        }
        composable(route = Rules.route) {
            ChallengeRulesScreen(navController)
        }
        composable(route = Settings.route) {
            SettingsScreen(
                navController = navController,
                onClickWeightTracker = {
                    navController.navigate(route = WeightTracker.route)
                },
                onClickWaterTracker = {
                    navController.navigate(route = WaterTracker.route)
                },
                onClickRules = {
                    navController.navigate(route = Rules.route)
                },
                onClickAbout = {
                    navController.navigate(route = Libraries.route)
                },
                onClickChallengeComplete = {
                    navController.navigate(route = ChallengeComplete.route)
                }
            )
        }
        composable(route = "dayscreen/{dayNumber}") { backStackEntry ->
            val dayNumber = backStackEntry.arguments?.getString("dayNumber") ?: "0"

            DayScreen(
                navController = navController,
                dayNumber = dayNumber
            )
        }
        composable(route = WeightTracker.route) {
            WeightTrackerScreen(navController)
        }
        composable(route = WaterTracker.route) {
            WaterTrackerScreen(navController)
        }
        composable(route = Rules.route) {
            ChallengeRulesScreen(navController)
        }
        composable(route = Libraries.route) {
            LibrariesScreen(navController)
        }
        composable(route = ChallengeComplete.route) {
            ChallengeCompleteScreen(navController)
        }
    }
}