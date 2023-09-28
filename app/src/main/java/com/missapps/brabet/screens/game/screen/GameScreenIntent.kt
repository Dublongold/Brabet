package com.missapps.brabet.screens.game.screen

import com.missapps.brabet.screens.game.GameItem

sealed interface GameScreenIntent {
    data class UpdateTime(val time: Int) : GameScreenIntent
    data class UpdateGoal(val goal: List<GameScreenState.Goal>) : GameScreenIntent
    data class UpdateLevel(val level: List<List<GameItem.ItemType>>) : GameScreenIntent
    data class UpdateNextLevel(val level: Int) : GameScreenIntent
    data object ShowHowToPlay : GameScreenIntent
    data object Pause : GameScreenIntent
    data object Resume : GameScreenIntent
    data class Start(val level: Int) : GameScreenIntent
    data object ShowWin : GameScreenIntent
    data object ShowLose : GameScreenIntent
}
