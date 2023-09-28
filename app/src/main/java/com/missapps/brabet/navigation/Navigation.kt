package com.missapps.brabet.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.missapps.brabet.core.viewmodel.ViewModelFactory
import com.missapps.brabet.screens.game.screen.GameScreen
import com.missapps.brabet.screens.levels.LevelsScreen
import com.missapps.brabet.screens.main.MainScreen
import com.missapps.brabet.screens.privacy.PrivacyPolicyScreen
import com.missapps.brabet.screens.splash.SplashScreen
import com.missapps.brabet.screens.webview.WebviewScreen

@Composable
@Suppress("FunctionNaming")
fun Navigation(
    viewModelFactory: ViewModelFactory,
    url: String
) {
    val navigator = rememberNavigator()
    NavHost(
        navController = navigator.navController,
        startDestination = NavigationRoute.SPLASH.route
    ) {
        composable(NavigationRoute.SPLASH.route) {
            SplashScreen(
                viewModel = viewModel(factory = viewModelFactory),
                navigateToMainScreen = navigator::navigateToMainScreen,
                navigateToWebviewScreen = navigator::navigateToWebviewScreen
            )
        }
        composable(NavigationRoute.MAIN.route) {
            MainScreen(
                viewModel = viewModel(factory = viewModelFactory),
                navigateToLevelsScreen = navigator::navigateToLevelsScreen,
                navigateToPrivacyPolicyScreen = navigator::navigateToPrivacyPolicyScreen
            )
        }
        composable(NavigationRoute.WEBVIEW.route) {
            WebviewScreen(url)
        }
        composable(NavigationRoute.PRIVACY_POLICY.route) {
            PrivacyPolicyScreen(onBack = navigator::back)
        }
        composable(NavigationRoute.LEVELS.route) {
            LevelsScreen(
                viewModel = viewModel(factory = viewModelFactory),
                onBack = navigator::back,
                navigateToGameScreen = navigator::navigateToGameScreen
            )
        }
        composable(
            route = NavigationRoute.GAME.route,
            arguments = listOf(navArgument(LEVEL_ARGUMENT_KEY) { type = NavType.IntType })
        ) {
            it.arguments?.getInt(LEVEL_ARGUMENT_KEY)?.let { level ->
                GameScreen(
                    level = level,
                    viewModel = viewModel(factory = viewModelFactory),
                    onBack = navigator::back,
                    navigateToMainMenu = navigator::popUpToMainScreen,
                    navigateToNextLevel = {
                        navigator.back()
                        navigator.navigateToGameScreen(it)
                    }
                )
            }
        }
    }
}
