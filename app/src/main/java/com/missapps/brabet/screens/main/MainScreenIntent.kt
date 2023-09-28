package com.missapps.brabet.screens.main

sealed interface MainScreenIntent {
    data object NavigateToPrivacyPolicyScreen : MainScreenIntent
    data object NavigateToLevelsScreen : MainScreenIntent
    data object NavigationEventConsumed : MainScreenIntent
    data object ShowDialog : MainScreenIntent
    data object HideDialog : MainScreenIntent
}
