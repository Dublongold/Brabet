package com.missapps.brabet.screens.splash

import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class SplashScreenState(
    val navigationEvent: StateEventWithContent<NavigationDestination> = consumed()
) {
    enum class NavigationDestination {
        MAIN, WEBVIEW
    }
}
