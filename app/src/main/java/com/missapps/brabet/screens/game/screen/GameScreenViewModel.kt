package com.missapps.brabet.screens.game.screen

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.missapps.brabet.core.viewmodel.BaseViewModel
import com.missapps.brabet.levels.GameInfoRepository
import com.missapps.brabet.screens.game.GameItem
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class GameScreenViewModel @Inject constructor(
    private val gameInfoRepository: GameInfoRepository
) : BaseViewModel<GameScreenState, GameScreenIntent>(GameScreenState()) {

    fun start(level: Int) {
        viewModelScope.launch {
            intents.send(GameScreenIntent.Start(level))
        }
    }

    fun pause() {
        viewModelScope.launch {
            intents.send(GameScreenIntent.Pause)
        }
    }

    fun resume() {
        viewModelScope.launch {
            intents.send(GameScreenIntent.Resume)
        }
    }

    fun showHowToPlay() {
        viewModelScope.launch {
            intents.send(GameScreenIntent.ShowHowToPlay)
        }
    }

    fun updateScore(type: GameItem.ItemType, points: Int) {
        viewModelScope.launch {
            intents.send(
                GameScreenIntent.UpdateGoal(
                    state.value.goals.map {
                        if (it.type == type) {
                            it.copy(
                                current = if (it.current + points <= it.expected) {
                                    it.current + points
                                } else {
                                    it.expected
                                }
                            )
                        } else {
                            it
                        }
                    }
                )
            )
        }
    }

    @Suppress("MagicNumber")
    override fun reduce(intent: GameScreenIntent): GameScreenState = when (intent) {
        GameScreenIntent.Pause -> state.value.copy(
            pause = true
        )

        GameScreenIntent.Resume -> state.value.copy(
            pause = false,
            showHowToPlay = false
        )

        GameScreenIntent.ShowHowToPlay -> state.value.copy(
            showHowToPlay = true
        )

        is GameScreenIntent.Start -> {
            startGame(intent.level)
            state.value.copy(win = false, lose = false, pause = false, showHowToPlay = false)
        }

        is GameScreenIntent.UpdateGoal -> state.value.copy(
            goals = intent.goal
        )

        is GameScreenIntent.UpdateTime -> state.value.copy(
            time = "%02d:%02d".format(intent.time / 60, intent.time % 60)
        )

        GameScreenIntent.ShowLose -> state.value.copy(
            lose = true
        )

        GameScreenIntent.ShowWin -> {
            viewModelScope.launch {
                val lastUnlockedLevel = gameInfoRepository.getUnlockedLevelsCount()
                if (lastUnlockedLevel < gameInfoRepository.getLevelsInfo().size) {
                    gameInfoRepository.setUnlockedLevelsCount(lastUnlockedLevel + 1)
                }
            }
            state.value.copy(
                win = true
            )
        }

        is GameScreenIntent.UpdateLevel -> state.value.copy(level = intent.level)
        is GameScreenIntent.UpdateNextLevel -> state.value.copy(nextLevel = intent.level)
    }

    private var timerJob: Job? = null

    private fun startGame(level: Int) {
        timerJob?.cancel()
        viewModelScope.launch {
            val levelsInfo = gameInfoRepository.getLevelsInfo()
            val currentLevelInfo = levelsInfo.first { it.level == level }
            val nextLevelInfoJob = launch {
                if (level < levelsInfo.size) {
                    intents.send(GameScreenIntent.UpdateNextLevel(level + 1))
                }
            }
            val levelJob = launch {
                intents.send(
                    GameScreenIntent.UpdateLevel(
                        currentLevelInfo.items.map { row ->
                            row.map {
                                GameItem.ItemType.entries[it - 1]
                            }
                        }
                    )
                )
            }
            val goalsInfoJob = launch {
                intents.send(
                    GameScreenIntent.UpdateGoal(
                        currentLevelInfo.goal.map {
                            GameScreenState.Goal(
                                current = 0,
                                expected = it.count,
                                type = GameItem.ItemType.entries[it.item - 1]
                            )
                        }
                    )
                )
            }
            joinAll(nextLevelInfoJob, levelJob, goalsInfoJob)
            timerJob = launch {
                var time = currentLevelInfo.timeInSeconds
                while (time > 0) {
                    if (state.value.goals.all { it.current == it.expected }) {
                        intents.send(GameScreenIntent.ShowWin)
                        break
                    }
                    intents.send(GameScreenIntent.UpdateTime(time))
                    delay(1.seconds)
                    if (!state.value.showHowToPlay && !state.value.pause) {
                        time--
                    }
                }
                intents.send(GameScreenIntent.UpdateTime(time))
                if (state.value.goals.any { it.current != it.expected }) {
                    intents.send(GameScreenIntent.ShowLose)
                }
            }
        }
    }
}
