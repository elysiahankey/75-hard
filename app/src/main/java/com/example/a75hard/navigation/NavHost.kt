package com.example.a75hard.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.a75hard.screens.ChallengeRulesScreen
import com.example.a75hard.screens.DayScreen
import com.example.a75hard.viewmodels.DayViewModel
import com.example.a75hard.screens.HomeScreen
import com.example.a75hard.viewmodels.HomeViewModel
import com.example.a75hard.screens.SettingsScreen
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
                    navController.navigate(DayScreen.createRoute(dayNumber.toString()))
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
                })
        }
        composable(route = "dayscreen/{dayNumber}") { backStackEntry ->
            val dayNumber = backStackEntry.arguments?.getString("dayNumber") ?: "0"
            val homeViewModel = hiltViewModel<HomeViewModel>()
            val dayViewModel = hiltViewModel<DayViewModel>()

            dayViewModel.bindHomeViewModel(homeViewModel)

            DayScreen(
                navController = navController,
                dayNumber = dayNumber
            )
        }
        composable(route = WeightTracker.route) {
            WeightTrackerScreen(navController)
        }
    }
}