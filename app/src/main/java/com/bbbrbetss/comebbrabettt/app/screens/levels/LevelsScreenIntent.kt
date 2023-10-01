package com.bbbrbetss.comebbrabettt.app.screens.levels

sealed interface LevelsScreenIntent {
    data class UpdateLevels(val count: Int, val unlockedLevelsCount: Int) : LevelsScreenIntent
    data class NavigateToGame(val level: Int) : LevelsScreenIntent
    data object NavigationEventConsumed : LevelsScreenIntent
}
