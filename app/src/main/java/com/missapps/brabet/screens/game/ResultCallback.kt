package com.missapps.brabet.screens.game

fun interface ResultCallback {
    fun onResult(gameItemType: GameItem.ItemType, points: Int)
}
