@file:Suppress("FunctionNaming")

package com.missapps.brabet.screens.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.missapps.brabet.R
import com.missapps.brabet.ui.backgroundColor
import de.palm.composestateevents.EventEffect

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel,
    navigateToMainScreen: () -> Unit,
    navigateToWebviewScreen: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EventEffect(
        event = state.navigationEvent,
        onConsumed = viewModel::navigationEventConsumed,
        action = {
            when (it) {
                SplashScreenState.NavigationDestination.MAIN -> navigateToMainScreen()
                SplashScreenState.NavigationDestination.WEBVIEW -> navigateToWebviewScreen()
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .paint(
                painter = painterResource(R.drawable.splash_screen_bg),
                contentScale = ContentScale.FillBounds
            )
            .padding(bottom = 64.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        ProgressBar()
    }
}

@Composable
private fun ProgressBar() {
    val transition = rememberInfiniteTransition(label = "progress bar animation")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ),
        label = "progress bar animation"
    )
    Image(
        modifier = Modifier.rotate(angle),
        painter = painterResource(R.drawable.progress_bar),
        contentDescription = null
    )
}
