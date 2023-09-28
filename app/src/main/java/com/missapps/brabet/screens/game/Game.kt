@file:Suppress("FunctionNaming")

package com.missapps.brabet.screens.game

import android.view.ViewGroup.LayoutParams
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun Game(
    modifier: Modifier = Modifier,
    callback: ResultCallback,
    level: List<List<GameItem.ItemType>>,
    attempt: Int
) {

    var gameView by remember { mutableStateOf<GameView?>(null) }
    LaunchedEffect(attempt) { gameView?.setLevel(level) }

    BoxWithConstraints(modifier = modifier) {
        val width = with(LocalDensity.current) { maxWidth.toPx().toInt() }
        val height = with(LocalDensity.current) { maxHeight.toPx().toInt() }
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                GameView(it).apply {
                    layoutParams = LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT
                    )
                    this.callback = callback
                    cellWidth =
                        (width - (GameView.WIDTH - 1) * GameView.SPACE_BETWEEN_CELLS_PX) / GameView.WIDTH
                    cellHeight =
                        (height - (GameView.HEIGHT - 1) * GameView.SPACE_BETWEEN_CELLS_PX) / GameView.HEIGHT
                    gameView = this
                }
            }
        )
    }
}
