package com.zhiguan.app.wear.ui

import androidx.compose.runtime.Composable
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.zhiguan.app.wear.ui.screens.WearAboutScreen
import com.zhiguan.app.wear.ui.screens.WearCalendarScreen
import com.zhiguan.app.wear.ui.screens.WearCheckInScreen
import com.zhiguan.app.wear.ui.screens.WearEmergencyScreen
import com.zhiguan.app.wear.ui.screens.WearHomeScreen
import com.zhiguan.app.wear.ui.screens.WearLearnScreen
import com.zhiguan.app.wear.ui.screens.WearSettingsScreen
import com.zhiguan.app.wear.ui.screens.WearStatsScreen

@Composable
fun WearApp() {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            WearHomeScreen(
                onCheckInClick = { navController.navigate("checkin") },
                onCalendarClick = { navController.navigate("calendar") },
                onStatsClick = { navController.navigate("stats") },
                onEmergencyClick = { navController.navigate("emergency") },
                onLearnClick = { navController.navigate("learn") },
                onSettingsClick = { navController.navigate("settings") },
                onAboutClick = { navController.navigate("about") }
            )
        }
        composable("checkin") {
            WearCheckInScreen(onBack = { navController.popBackStack() })
        }
        composable("calendar") {
            WearCalendarScreen(onBack = { navController.popBackStack() })
        }
        composable("stats") {
            WearStatsScreen(onBack = { navController.popBackStack() })
        }
        composable("emergency") {
            WearEmergencyScreen(onBack = { navController.popBackStack() })
        }
        composable("learn") {
            WearLearnScreen(onBack = { navController.popBackStack() })
        }
        composable("settings") {
            WearSettingsScreen(onBack = { navController.popBackStack() })
        }
        composable("about") {
            WearAboutScreen(onBack = { navController.popBackStack() })
        }
    }
}