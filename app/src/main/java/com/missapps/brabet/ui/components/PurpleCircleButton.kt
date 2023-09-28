package com.missapps.brabet.ui.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.missapps.brabet.ui.purple

@Composable
@Suppress("FunctionNaming")
fun PurpleCircleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = purple,
        shape = CircleShape,
        content = content
    )
}
