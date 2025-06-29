package com.example.a75hard.navigation

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.a75hard.R

interface Destination {
    val name: String
    val route: String
    val icon: @Composable () -> Unit
}

object Home : Destination {
    override val name = "Home"
    override val route = "home"
    override val icon = @Composable {
        Icon(
            painterResource(R.drawable.ic_search),
            contentDescription = null
        )
    }
}

object Rules : Destination {
    override val name = "Rules"
    override val route = "rules"
    override val icon = @Composable {
        Icon(
            painterResource(R.drawable.ic_search),
            contentDescription = null
        )
    }
}

object Settings : Destination {
    override val name = "Settings"
    override val route = "settings"
    override val icon = @Composable {
        Icon(
            painterResource(R.drawable.ic_search),
            contentDescription = null
        )
    }
}

object DayScreen : Destination {
    override val name = "Day Screen"
    override val route = "dayscreen/{day}"

    override val icon: @Composable () -> Unit = {
        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = null
        )
    }

    fun createRoute(day: String): String = "dayscreen/$day"
}

object WeightTracker: Destination {
    override val name = "Weight Tracker"
    override val route = "weighttracker"

    override val icon: @Composable () -> Unit = {
        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = null
        )
    }
}

object WaterTracker: Destination {
    override val name = "Water Tracker"
    override val route = "watertracker"

    override val icon: @Composable () -> Unit = {
        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = null
        )
    }
}

object Libraries: Destination {
    override val name = "Libraries"
    override val route = "libraries"

    override val icon: @Composable () -> Unit = {
        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = null
        )
    }
}

val topLevelRoutes = listOf(Home, Settings)