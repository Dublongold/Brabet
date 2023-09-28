package com.missapps.brabet.screens.main

import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class MainScreenState(
    val navigationEvent: StateEventWithContent<NavigationDestination> = consumed(),
    val isDialogOpened: Boolean = false
) {
    enum class NavigationDestination { LEVELS, PRIVACY_POLICY }
}
