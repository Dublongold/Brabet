package com.bbbrbetss.comebbrabettt.app.screens.game.screen

import com.bbbrbetss.comebbrabettt.app.screens.game.GameItem

data class GameScreenState(
    val time: String = "00:00",
    val goals: List<Goal> = emptyList(),
    val showHowToPlay: Boolean = false,
    val pause: Boolean = false,
    val win: Boolean = false,
    val lose: Boolean = false,
    val level: List<List<GameItem.ItemType>> = emptyList(),
    val nextLevel: Int? = null
) {
    data class Goal(val current: Int, val expected: Int, val type: GameItem.ItemType)
}
