package com.bbbrbetss.comebbrabettt.app.screens.main

import androidx.lifecycle.viewModelScope
import com.bbbrbetss.comebbrabettt.app.core.viewmodel.BaseViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import javax.inject.Inject
import kotlinx.coroutines.launch

class MainScreenViewModel @Inject constructor() :
    BaseViewModel<MainScreenState, MainScreenIntent>(MainScreenState()) {

    fun showHowToPlayDialog() {
        viewModelScope.launch {
            intents.send(MainScreenIntent.ShowDialog)
        }
    }

    fun hideDialog() {
        viewModelScope.launch {
            intents.send(MainScreenIntent.HideDialog)
        }
    }

    fun navigateToLevelsScreen() {
        viewModelScope.launch {
            intents.send(MainScreenIntent.NavigateToLevelsScreen)
        }
    }

    fun navigateToPrivacyPolicyScreen() {
        viewModelScope.launch {
            intents.send(MainScreenIntent.NavigateToPrivacyPolicyScreen)
        }
    }

    fun navigationEventConsumed() {
        viewModelScope.launch {
            intents.send(MainScreenIntent.NavigationEventConsumed)
        }
    }

    override fun reduce(intent: MainScreenIntent): MainScreenState = when (intent) {
        MainScreenIntent.NavigateToLevelsScreen -> state.value.copy(
            navigationEvent = triggered(MainScreenState.NavigationDestination.LEVELS)
        )

        MainScreenIntent.NavigateToPrivacyPolicyScreen -> state.value.copy(
            navigationEvent = triggered(MainScreenState.NavigationDestination.PRIVACY_POLICY)
        )

        MainScreenIntent.NavigationEventConsumed -> state.value.copy(
            navigationEvent = consumed()
        )

        MainScreenIntent.ShowDialog -> state.value.copy(isDialogOpened = true)
        MainScreenIntent.HideDialog -> state.value.copy(isDialogOpened = false)
    }
}
