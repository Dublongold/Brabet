package com.missapps.brabet.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.missapps.brabet.ui.purple

@Composable
@Suppress("FunctionNaming")
fun PurpleButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = purple)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 16.dp),
            text = text
        )
    }
}
