package com.example.communityevents.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.communityevents.ui.components.BottomNavBar
import com.example.communityevents.ui.screens.AddEventScreen
import com.example.communityevents.ui.screens.ExploreScreen
import com.example.communityevents.ui.screens.ProfileScreen
import com.example.communityevents.ui.screens.SavedScreen

object Routes {
    const val EXPLORE = "explore"
    const val SAVED   = "saved"
    const val ADD     = "add"
    const val PROFILE = "profile"
}

@Composable
fun CommunityEventsApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onItemClick = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.EXPLORE,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.EXPLORE) { ExploreScreen() }
            composable(Routes.SAVED)   { SavedScreen() }
            composable(Routes.ADD)     { AddEventScreen(onClose = { navController.popBackStack() }) }
            composable(Routes.PROFILE) { ProfileScreen() }
        }
    }
}