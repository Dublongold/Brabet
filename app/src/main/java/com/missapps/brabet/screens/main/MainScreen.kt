package com.missapps.brabet.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.missapps.brabet.R
import com.missapps.brabet.ui.backgroundColor
import com.missapps.brabet.ui.components.HowToPlayDialog
import com.missapps.brabet.ui.components.PurpleButton
import com.missapps.brabet.ui.components.PurpleCircleButton
import de.palm.composestateevents.EventEffect

@Composable
@Suppress("FunctionNaming", "LongMethod")
fun MainScreen(
    viewModel: MainScreenViewModel,
    navigateToLevelsScreen: () -> Unit,
    navigateToPrivacyPolicyScreen: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    EventEffect(
        event = state.navigationEvent,
        onConsumed = viewModel::navigationEventConsumed,
        action = {
            when (it) {
                MainScreenState.NavigationDestination.LEVELS -> navigateToLevelsScreen()
                MainScreenState.NavigationDestination.PRIVACY_POLICY -> navigateToPrivacyPolicyScreen()
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .paint(
                painter = painterResource(R.drawable.main_screen_bg),
                contentScale = ContentScale.FillBounds
            )
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            PurpleButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.play_game_button_text),
                onClick = viewModel::navigateToLevelsScreen
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PurpleCircleButton(
                    modifier = Modifier.size(64.dp),
                    onClick = viewModel::navigateToPrivacyPolicyScreen
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_privacy_policy_button),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                PurpleCircleButton(
                    modifier = Modifier.size(64.dp),
                    onClick = viewModel::showHowToPlayDialog
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_how_to_play_button),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
        if (state.isDialogOpened) {
            HowToPlayDialog(onDismissRequest = viewModel::hideDialog)
        }
    }
}
