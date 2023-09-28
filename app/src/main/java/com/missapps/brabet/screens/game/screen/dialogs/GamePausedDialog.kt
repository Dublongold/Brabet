package com.missapps.brabet.screens.game.screen.dialogs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.missapps.brabet.R
import com.missapps.brabet.ui.components.Dialog
import com.missapps.brabet.ui.components.PurpleButton
import com.missapps.brabet.ui.components.Title

@Composable
fun GamePausedDialog(
    resume: () -> Unit,
    restart: () -> Unit,
    navigateToMainMenu: () -> Unit,
    level: Int,
    time: String
) {
    Dialog(
        title = stringResource(R.string.paused_game_dialog_title),
        onDismissRequest = resume
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
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
        Spacer(modifier = Modifier.height(16.dp))
        PurpleButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.resume_button_text),
            onClick = resume
        )
        Spacer(modifier = Modifier.height(8.dp))
        PurpleButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.restart_game_button_text),
            onClick = restart
        )
        Spacer(modifier = Modifier.height(8.dp))
        PurpleButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.main_menu_button_text),
            onClick = navigateToMainMenu
        )
    }
}
