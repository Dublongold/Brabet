package com.bbbrbetss.comebbrabettt.app.screens.game

data class GameItem(
    val poseX: Int,
    val poseY: Int,
    val type: ItemType?
) {
    enum class ItemType {
        BALL, FLAGS, FOOTWEAR, TIMER, WHISTLE
    }
}
