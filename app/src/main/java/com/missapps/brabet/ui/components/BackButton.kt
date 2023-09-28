package com.missapps.brabet.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.missapps.brabet.R

@Composable
@Suppress("FunctionNaming")
fun BackButton(modifier: Modifier = Modifier, onBack: () -> Unit) {
    PurpleCircleButton(
        modifier = modifier.size(48.dp),
        onClick = onBack
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = null,
            tint = Color.White
        )
    }
}