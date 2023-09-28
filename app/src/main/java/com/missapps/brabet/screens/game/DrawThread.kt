package com.missapps.brabet.screens.game

class DrawThread(private val gameView: GameView) : Thread() {
    var running: Boolean = false

    override fun run() {
        var fDeltaTime = 0.0
        var uDeltaTime = 0.0

        var frames = 0
        var updates = 0

        var startTime = System.nanoTime()
        var timer = System.currentTimeMillis()

        while (running) {
            val currentTime = System.nanoTime()
            fDeltaTime += (currentTime - startTime)
            uDeltaTime += (currentTime - startTime)
            startTime = currentTime

            if (fDeltaTime >= F_OPTIMAL_TIME) {
                gameView.holder.lockCanvas(null)?.let {
                    synchronized(gameView.holder) {
                        gameView.draw(it)
                    }
                    if (gameView.isDestroyed) gameView.holder.unlockCanvasAndPost(it)
                }
                frames++
                fDeltaTime - +F_OPTIMAL_TIME
            }

            if (uDeltaTime >= U_OPTIMAL_TIME) {
                gameView.update()
                updates++
                uDeltaTime -= U_OPTIMAL_TIME
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                frames = 0
                updates = 0
                timer += 100
            }
        }
    }

    private companion object {
        const val MAX_FPS = 60
        const val MAX_UPS = 60

        const val F_OPTIMAL_TIME = 1000_000_000.0 / MAX_FPS
        const val U_OPTIMAL_TIME = 1000_000_000.0 / MAX_UPS
    }
}

