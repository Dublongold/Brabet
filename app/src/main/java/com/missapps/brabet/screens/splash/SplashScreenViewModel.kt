package com.missapps.brabet.screens.splash

import androidx.lifecycle.viewModelScope
import com.missapps.brabet.core.viewmodel.BaseViewModel
import com.missapps.brabet.firebase.FirebaseRemoteConfigsRepository
import com.missapps.brabet.levels.GameInfoRepository
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import javax.inject.Inject
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class SplashScreenViewModel @Inject constructor(
    private val firebaseRemoteConfigsRepository: FirebaseRemoteConfigsRepository,
    private val gameInfoRepository: GameInfoRepository
) : BaseViewModel<SplashScreenState, SplashScreenIntent>(SplashScreenState()) {

    init {
        viewModelScope.launch {
            joinAll(
                launch { firebaseRemoteConfigsRepository.startInitialFetch() },
                launch { gameInfoRepository.getLevelsInfo() }
            )
            intents.send(
                if (firebaseRemoteConfigsRepository.showWebview) {
                    SplashScreenIntent.NavigateToWebviewScreen
                } else {
                    SplashScreenIntent.NavigateToMainScreen
                }
            )
        }
    }

    fun navigationEventConsumed() {
        viewModelScope.launch {
            intents.send(SplashScreenIntent.NavigationEventConsumed)
        }
    }

    override fun reduce(intent: SplashScreenIntent): SplashScreenState = when (intent) {
        SplashScreenIntent.NavigateToMainScreen -> SplashScreenState(
            navigationEvent = triggered(
                SplashScreenState.NavigationDestination.MAIN
            )
        )

        SplashScreenIntent.NavigateToWebviewScreen -> SplashScreenState(
            navigationEvent = triggered(
                SplashScreenState.NavigationDestination.WEBVIEW
            )
        )

        SplashScreenIntent.NavigationEventConsumed -> SplashScreenState(navigationEvent = consumed())
    }
}
