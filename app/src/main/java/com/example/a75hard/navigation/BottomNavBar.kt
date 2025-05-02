package com.example.a75hard.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun BottomNavBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        topLevelRoutes.forEach { topLevelRoute ->
            NavigationBarItem(
                icon = { topLevelRoute.icon },
                label = { Text(topLevelRoute.name) },
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(
                        topLevelRoute.route::class
                    )
                } == true,
                onClick = {
                    navController.navigateTo(topLevelRoute.route)
                }
            )
        }
    }
}

@Preview
@Composable
fun BottomNavBarPreview() {
    var navController = rememberNavController()
    BottomNavBar(navController)
}