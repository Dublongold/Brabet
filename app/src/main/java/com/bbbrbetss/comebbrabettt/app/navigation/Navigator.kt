package com.bbbrbetss.comebbrabettt.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class Navigator(val navController: NavHostController) {
    fun navigateToMainScreen() {
        navController.popBackStack()
        navController.navigate(NavigationRoute.MAIN.route)
    }

    fun popUpToMainScreen() {
        navController.popBackStack(route = NavigationRoute.MAIN.route, inclusive = false)
    }

    fun navigateToWebviewScreen() {
        navController.popBackStack()
        navController.navigate(NavigationRoute.WEBVIEW.route)
    }

    fun navigateToLevelsScreen() {
        navController.navigate(NavigationRoute.LEVELS.route)
    }

    fun navigateToPrivacyPolicyScreen() {
        navController.navigate(NavigationRoute.PRIVACY_POLICY.route)
    }

    fun navigateToGameScreen(level: Int) {
        navController.navigate(
            NavigationRoute.GAME.route.replace(
                "{$LEVEL_ARGUMENT_KEY}",
                level.toString()
            )
        )
    }

    fun back() {
        navController.popBackStack()
    }
}

@Composable
fun rememberNavigator(): Navigator {
    val navController = rememberNavController()
    return remember { Navigator(navController) }
}
