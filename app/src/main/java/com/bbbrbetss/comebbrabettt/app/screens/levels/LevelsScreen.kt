@file:Suppress("FunctionNaming")

package com.bbbrbetss.comebbrabettt.app.screens.levels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bbbrbetss.comebbrabettt.app.R
import com.bbbrbetss.comebbrabettt.app.ui.backgroundColor
import com.bbbrbetss.comebbrabettt.app.ui.blue
import com.bbbrbetss.comebbrabettt.app.ui.components.BackButton
import com.bbbrbetss.comebbrabettt.app.ui.components.PurpleCircleButton
import com.bbbrbetss.comebbrabettt.app.ui.components.Title
import com.bbbrbetss.comebbrabettt.app.ui.lightBlue
import de.palm.composestateevents.EventEffect

@Composable
fun LevelsScreen(
    viewModel: LevelsScreenViewModel,
    onBack: () -> Unit,
    navigateToGameScreen: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EventEffect(
        event = state.navigationEvent,
        onConsumed = viewModel::navigationEventConsumed,
        action = navigateToGameScreen
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            BackButton(modifier = Modifier.align(Alignment.CenterStart), onBack = onBack)
            Title(
                modifier = Modifier
                    .width(160.dp)
                    .align(Alignment.Center),
                text = stringResource(R.string.select_level_title)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        state.levelsInfo?.let {
            Levels(
                levelsInfo = it,
                navigateToGameScreen = viewModel::navigateToGameScreen
            )
        }
    }
}

private const val LEVELS_IN_ROW = 6

@Composable
private fun ColumnScope.Levels(
    levelsInfo: LevelsScreenState.LevelsInfo,
    navigateToGameScreen: (Int) -> Unit
) {
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        List(levelsInfo.count) { index -> index + 1 }
            .chunked(LEVELS_IN_ROW)
            .forEach { levelsRow ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    levelsRow.forEach { level ->
                        if (level > levelsInfo.unlockedLevelsCount) LockedLevel()
                        else UnlockedLevel(
                            level = level,
                            navigateToGameScreen = navigateToGameScreen
                        )
                    }
                }
            }
    }
}

@Composable
private fun UnlockedLevel(
    level: Int,
    navigateToGameScreen: (Int) -> Unit
) {
    PurpleCircleButton(
        modifier = Modifier.size(48.dp),
        onClick = { navigateToGameScreen(level) }
    ) { Text(text = level.toString(), fontSize = 16.sp, color = Color.White) }
}

@Composable
private fun LockedLevel() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(lightBlue, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_lock),
            contentDescription = null,
            tint = blue
        )
    }
}
