package com.example.a75hard.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.a75hard.ChallengeRulesScreen
import com.example.a75hard.DayScreen
import com.example.a75hard.HomeScreen

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
        composable(route = "dayscreen/{day}") { backStackEntry ->
            val day = backStackEntry.arguments?.getString("day") ?: "1" // default fallback
            DayScreen(day = day)
        }
    }
}