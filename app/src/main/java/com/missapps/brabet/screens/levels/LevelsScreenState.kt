package com.missapps.brabet.screens.levels

import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class LevelsScreenState(
    val levelsInfo: LevelsInfo? = null,
    val navigationEvent: StateEventWithContent<Int> = consumed()
) {
    data class LevelsInfo(val count: Int, val unlockedLevelsCount: Int)
}
