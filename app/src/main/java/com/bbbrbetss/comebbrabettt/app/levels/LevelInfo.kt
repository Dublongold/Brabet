package com.bbbrbetss.comebbrabettt.app.levels

import kotlinx.serialization.Serializable

@Serializable
data class LevelInfo(
    val level: Int,
    val timeInSeconds: Int,
    val goal: List<GoalItem>,
    val items: List<List<Int>>
)

@Serializable
data class GoalItem(
    val item: Int,
    val count: Int
)
