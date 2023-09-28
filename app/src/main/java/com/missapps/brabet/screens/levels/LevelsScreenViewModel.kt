package com.missapps.brabet.screens.levels

import androidx.lifecycle.viewModelScope
import com.missapps.brabet.core.viewmodel.BaseViewModel
import com.missapps.brabet.levels.GameInfoRepository
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LevelsScreenViewModel @Inject constructor(
    private val gameInfoRepository: GameInfoRepository
) : BaseViewModel<LevelsScreenState, LevelsScreenIntent>(LevelsScreenState()) {

    init {
        val levelsCount = viewModelScope.async { gameInfoRepository.getLevelsInfo().size }
        gameInfoRepository.getUnlockedLevelsCountFlow()
            .onEach {
                intents.send(
                    LevelsScreenIntent.UpdateLevels(
                        count = levelsCount.await(),
                        unlockedLevelsCount = it
                    )
                )
            }.launchIn(viewModelScope)
    }

    fun navigateToGameScreen(level: Int) {
        viewModelScope.launch { intents.send(LevelsScreenIntent.NavigateToGame(level)) }
    }

    fun navigationEventConsumed() {
        viewModelScope.launch { intents.send(LevelsScreenIntent.NavigationEventConsumed) }
    }

    override fun reduce(intent: LevelsScreenIntent): LevelsScreenState = when (intent) {
        is LevelsScreenIntent.NavigateToGame -> state.value.copy(navigationEvent = triggered(intent.level))
        LevelsScreenIntent.NavigationEventConsumed -> state.value.copy(navigationEvent = consumed())
        is LevelsScreenIntent.UpdateLevels -> state.value.copy(
            levelsInfo = LevelsScreenState.LevelsInfo(
                count = intent.count,
                unlockedLevelsCount = intent.unlockedLevelsCount
            )
        )
    }
}
