@file:Suppress("FunctionNaming")

package com.missapps.brabet.screens.game.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.missapps.brabet.R
import com.missapps.brabet.screens.game.Game
import com.missapps.brabet.screens.game.GameItem
import com.missapps.brabet.screens.game.screen.dialogs.GamePausedDialog
import com.missapps.brabet.screens.game.screen.dialogs.LoseDialog
import com.missapps.brabet.screens.game.screen.dialogs.WinDialog
import com.missapps.brabet.ui.backgroundColor
import com.missapps.brabet.ui.components.BackButton
import com.missapps.brabet.ui.components.HowToPlayDialog
import com.missapps.brabet.ui.components.PurpleCircleButton
import com.missapps.brabet.ui.components.Title
import com.missapps.brabet.ui.lightBlue

@Composable
fun GameScreen(
    level: Int,
    viewModel: GameScreenViewModel,
    onBack: () -> Unit,
    navigateToMainMenu: () -> Unit,
    navigateToNextLevel: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(level) { viewModel.start(level) }
    var attempt by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Header(
            level = level,
            time = state.time,
            onBack = onBack
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (state.level.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(5f / 6f)
                    .background(lightBlue)
            ) {
                Game(
                    modifier = Modifier.fillMaxSize(),
                    callback = viewModel::updateScore,
                    level = state.level,
                    attempt = attempt
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Footer(
            pause = viewModel::pause,
            showHowToPlay = viewModel::showHowToPlay,
            goals = state.goals
        )
    }
    when {
        state.showHowToPlay -> HowToPlayDialog(viewModel::resume)
        state.pause -> GamePausedDialog(
            resume = viewModel::resume,
            restart = {
                attempt += 1
                viewModel.start(level)
            },
            navigateToMainMenu = navigateToMainMenu,
            level = level,
            time = state.time
        )

        state.win -> WinDialog(
            restart = {
                attempt += 1
                viewModel.start(level)
            },
            nextLevel = { state.nextLevel?.let(navigateToNextLevel) ?: onBack() },
            navigateToMainMenu = navigateToMainMenu,
            level = level,
            time = state.time
        )

        state.lose -> LoseDialog(
            restart = {
                attempt += 1
                viewModel.start(level)
            },
            navigateToMainMenu = navigateToMainMenu,
            level = level,
            time = state.time
        )
    }
}

@Composable
private fun Header(
    level: Int,
    time: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackButton(onBack = onBack)
        Spacer(modifier = Modifier.width(8.dp))
        Title(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.level_title, level)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Title(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.timer_text, time)
        )
    }
}

@Composable
private fun Footer(
    pause: () -> Unit,
    showHowToPlay: () -> Unit,
    goals: List<GameScreenState.Goal>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PurpleCircleButton(
            modifier = Modifier.size(48.dp),
            onClick = pause
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_pause),
                contentDescription = null,
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .weight(1f)
                .background(lightBlue, CircleShape),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                goals.forEach { GoalItem(it) }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        PurpleCircleButton(
            modifier = Modifier.size(48.dp),
            onClick = showHowToPlay
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_how_to_play_button),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
private fun GoalItem(goal: GameScreenState.Goal) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "${goal.current}/${goal.expected}", color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Image(
            modifier = Modifier.size(40.dp),
            painter = painterResource(
                id = when (goal.type) {
                    GameItem.ItemType.BALL -> R.drawable.ic_ball_small
                    GameItem.ItemType.FLAGS -> R.drawable.ic_flags_small
                    GameItem.ItemType.FOOTWEAR -> R.drawable.ic_footwear_small
                    GameItem.ItemType.TIMER -> R.drawable.ic_timer_small
                    GameItem.ItemType.WHISTLE -> R.drawable.ic_whistle_small
                }
            ),
            contentDescription = null
        )
    }
}
