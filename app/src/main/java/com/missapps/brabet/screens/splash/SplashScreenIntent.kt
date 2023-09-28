package com.missapps.brabet.screens.splash

sealed interface SplashScreenIntent {
    data object NavigateToMainScreen : SplashScreenIntent
    data object NavigateToWebviewScreen : SplashScreenIntent
    data object NavigationEventConsumed : SplashScreenIntent
}
