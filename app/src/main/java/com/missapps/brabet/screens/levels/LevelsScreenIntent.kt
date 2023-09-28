package com.missapps.brabet.screens.levels

sealed interface LevelsScreenIntent {
    data class UpdateLevels(val count: Int, val unlockedLevelsCount: Int) : LevelsScreenIntent
    data class NavigateToGame(val level: Int) : LevelsScreenIntent
    data object NavigationEventConsumed : LevelsScreenIntent
}
