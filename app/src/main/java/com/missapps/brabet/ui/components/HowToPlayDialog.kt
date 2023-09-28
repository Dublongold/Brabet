@file:Suppress("FunctionNaming")

package com.missapps.brabet.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.missapps.brabet.R

@Composable
fun HowToPlayDialog(onDismissRequest: () -> Unit) {
    Dialog(
        title = stringResource(R.string.how_to_play_dialog_title),
        onDismissRequest = onDismissRequest
    ) {
        Text(
            text = stringResource(R.string.how_to_play_dialog_text),
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        PurpleButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.ok_button_text),
            onClick = onDismissRequest
        )
    }
}

@Composable
@Preview(showBackground = true)
fun HowToPlayDialogPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        HowToPlayDialog(onDismissRequest = {})
    }
}
