package com.missapps.brabet.navigation

const val LEVEL_ARGUMENT_KEY = "LEVEL"

enum class NavigationRoute(val route: String) {
    SPLASH("SPLASH"),
    MAIN("MAIN"),
    WEBVIEW("WEBVIEW"),
    PRIVACY_POLICY("PRIVACY_POLICY"),
    LEVELS("LEVELS"),
    GAME("GAME/{$LEVEL_ARGUMENT_KEY}")
}
