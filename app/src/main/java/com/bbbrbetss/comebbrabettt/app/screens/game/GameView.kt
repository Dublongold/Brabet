package com.bbbrbetss.comebbrabettt.app.screens.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import com.bbbrbetss.comebbrabettt.app.R
import kotlin.math.abs
import kotlin.properties.Delegates

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : SurfaceView(context, attrs, defStyleAttr, defStyleRes), SurfaceHolder.Callback {

    private val drawThread = DrawThread(this)

    init {
        holder.addCallback(this)
    }

    var callback: ResultCallback? = null

    var cellWidth: Int by Delegates.notNull()
    var cellHeight: Int by Delegates.notNull()

    private val board: Array<Array<GameItem>> = Array(HEIGHT) {
        Array(WIDTH) { GameItem(0, 0, null) }
    }

    fun setLevel(level: List<List<GameItem.ItemType>>) {
        level.forEachIndexed { i, row ->
            row.forEachIndexed { j, itemType ->
                board[i][j] = GameItem(
                    poseX = j * (cellWidth + SPACE_BETWEEN_CELLS_PX),
                    poseY = i * (cellHeight + SPACE_BETWEEN_CELLS_PX),
                    type = itemType
                )
            }
        }
    }

    private val topBoard: Array<GameItem> by lazy {
        Array(board[0].size) { GameItem(cellWidth * it, -cellHeight, null) }
    }

    private var search = mutableListOf<MutableList<Point>>()

    private var oldX = 0f
    private var oldY = 0f

    private var poseI = 0
    private var poseJ = 0
    private var newPoseI = 0
    private var newPoseJ = 0

    private var direction: Direction by Delegates.notNull()

    private var move = false

    private var gameState = GameState.NOTHING

    private var swapIndex = 8

    private var dropStop = false

    private enum class GameState {
        SWAPPING, CHECK_SWAPPING, CRUSHING, UPDATE, NOTHING
    }

    private enum class Direction {
        RIGHT, LEFT, UP, DOWN
    }

    private val itemsBitmaps: Map<GameItem.ItemType, Bitmap> by lazy {
        GameItem.ItemType.entries.associateWith {
            Bitmap.createScaledBitmap(
                BitmapFactory.decodeStream(
                    context.assets.open(
                        when (it) {
                            GameItem.ItemType.BALL -> "ball_game_item.png"
                            GameItem.ItemType.FLAGS -> "flags_game_item.png"
                            GameItem.ItemType.FOOTWEAR -> "footwear_game_item.png"
                            GameItem.ItemType.TIMER -> "timer_game_item.png"
                            GameItem.ItemType.WHISTLE -> "whistle_game_item.png"
                        }
                    )
                ),
                cellWidth,
                cellHeight,
                false
            )
        }
    }

    private val frameBitmap: Bitmap by lazy {
        Bitmap.createScaledBitmap(
            BitmapFactory.decodeStream(
                context.assets.open("frame.png")
            ),
            cellWidth,
            cellHeight,
            false
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                oldX = event.x
                oldY = event.y
                poseJ = (oldX / cellWidth).toInt()
                poseI = (oldY / cellHeight).toInt()
                move = true
            }

            MotionEvent.ACTION_MOVE -> {
                if (gameState == GameState.NOTHING) {
                    val newX = event.x
                    val newY = event.y
                    val deltaX = abs(newX - oldX)
                    val deltaY = abs(newY - oldY)
                    if (move && (deltaX > MIN_DELTA_FOR_DRUGGING_PX || deltaY > MIN_DELTA_FOR_DRUGGING_PX)) {
                        move = false
                        if (deltaX > deltaY) {
                            if (newX > oldX) {
                                direction = Direction.RIGHT
                                newPoseJ = poseJ + 1
                            } else {
                                direction = Direction.LEFT
                                newPoseJ = poseJ - 1
                            }
                            newPoseI = poseI
                        }
                        if (deltaY > deltaX) {
                            if (newY > oldY) {
                                direction = Direction.DOWN
                                newPoseI = poseI + 1
                            } else {
                                direction = Direction.UP
                                newPoseI = poseI - 1
                            }
                            newPoseJ = poseJ
                        }
                        gameState = GameState.SWAPPING
                    }
                }
            }
        }
        return true
    }

    fun update() {
        when (gameState) {
            GameState.SWAPPING -> swap()
            GameState.CHECK_SWAPPING -> {
                fillCrushing()
                if (search.isEmpty()) swap()
                else gameState = GameState.CRUSHING
            }

            GameState.CRUSHING -> {
                search.forEach { points ->
                    board[points.first().x][points.first().y].type?.let {
                        callback?.onResult(it, points.size - 2)
                    }
                    points.forEach {
                        board[it.x][it.y] = board[it.x][it.y].copy(type = null)
                    }
                }
                search.clear()
                gameState = GameState.UPDATE
            }

            GameState.UPDATE -> {
                drop()
                fillTopBoard()
                fillCrushing()
                if (search.isEmpty()) {
                    if (!checkDrop()) {
                        gameState = GameState.NOTHING
                    }
                } else {
                    gameState = GameState.CRUSHING
                }
                dropStop = false
            }

            else -> {}
        }
    }

    private fun checkDrop() = board.any { row ->
        row.any { it.type == null }
    }


    private fun fillTopBoard() {
        for (j in topBoard.indices) {
            if (topBoard[j].type == null) {
                if (j == 0) {
                    topBoard[j] = topBoard[j].copy(type = GameItem.ItemType.entries.random())
                } else {
                    topBoard[j] =
                        topBoard[j].copy(
                            type = GameItem.ItemType.entries
                                .filter { it != topBoard[j - 1].type }
                                .random()
                        )
                }
            }
        }
    }

    private fun drop() {
        for (k in topBoard.indices) {
            if (board[0][k].type == null) {
                topBoard[k] = topBoard[k].copy(poseY = topBoard[k].poseY + cellHeight / 8)
                if (-topBoard[k].poseY < cellHeight / 8) {
                    board[0][k] = board[0][k].copy(type = topBoard[k].type)
                    topBoard[k] = topBoard[k].copy(
                        poseY = board[0][k].poseY - cellHeight,
                        poseX = k * cellWidth,
                        type = null
                    )
                    dropStop = true
                }
            }
        }
        for (i in 0 until board.size - 1) {
            for (j in 0 until board[i].size) {
                if (board[i][j].type != null) {
                    if (board[i + 1][j].type == null) {
                        board[i][j] = board[i][j].copy(poseY = board[i][j].poseY + cellHeight / 8)
                        if ((i + 1) * cellHeight - board[i][j].poseY < cellHeight / 8) {
                            board[i + 1][j] = board[i + 1][j].copy(type = board[i][j].type)
                            board[i][j] = GameItem(
                                j * (cellWidth + SPACE_BETWEEN_CELLS_PX),
                                i * (cellHeight + SPACE_BETWEEN_CELLS_PX),
                                null
                            )
                            dropStop = true
                        }
                    }
                }
            }
        }
    }

    private fun fillCrushing() {
        search.clear()
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j].type != null) {
                    if (j < board[i].size - 4 &&
                        board[i][j].type == board[i][j + 1].type &&
                        board[i][j + 1].type == board[i][j + 2].type &&
                        board[i][j + 2].type == board[i][j + 3].type &&
                        board[i][j + 3].type == board[i][j + 4].type
                    ) {
                        search.add(
                            mutableListOf(
                                Point(i, j),
                                Point(i, j + 1),
                                Point(i, j + 2),
                                Point(i, j + 3),
                                Point(i, j + 4)
                            )
                        )
                        break
                    } else if (j < board[i].size - 3 &&
                        board[i][j].type == board[i][j + 1].type &&
                        board[i][j + 1].type == board[i][j + 2].type &&
                        board[i][j + 2].type == board[i][j + 3].type
                    ) {
                        search.add(
                            mutableListOf(
                                Point(i, j),
                                Point(i, j + 1),
                                Point(i, j + 2),
                                Point(i, j + 3)
                            )
                        )
                        break
                    } else if (j < board[i].size - 2 &&
                        board[i][j].type == board[i][j + 1].type &&
                        board[i][j + 1].type == board[i][j + 2].type
                    ) {
                        search.add(mutableListOf(Point(i, j), Point(i, j + 1), Point(i, j + 2)))
                        break
                    }
                }
            }
        }
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j].type != null) {
                    if (i < board.size - 4 &&
                        board[i][j].type == board[i + 1][j].type &&
                        board[i + 1][j].type == board[i + 2][j].type &&
                        board[i + 2][j].type == board[i + 3][j].type &&
                        board[i + 3][j].type == board[i + 4][j].type
                    ) {
                        search.add(
                            mutableListOf(
                                Point(i, j),
                                Point(i + 1, j),
                                Point(i + 2, j),
                                Point(i + 3, j),
                                Point(i + 4, j)
                            )
                        )
                        break
                    } else if (i < board.size - 3 &&
                        board[i][j].type == board[i + 1][j].type &&
                        board[i + 1][j].type == board[i + 2][j].type &&
                        board[i + 2][j].type == board[i + 3][j].type
                    ) {
                        search.add(
                            mutableListOf(
                                Point(i, j),
                                Point(i + 1, j),
                                Point(i + 2, j),
                                Point(i + 3, j)
                            )
                        )
                        break
                    } else if (i < board.size - 2 &&
                        board[i][j].type == board[i + 1][j].type &&
                        board[i + 1][j].type == board[i + 2][j].type
                    ) {
                        search.add(
                            mutableListOf(
                                Point(i, j),
                                Point(i + 1, j),
                                Point(i + 2, j),
                            )
                        )
                        break
                    }
                }
            }
        }
        search = search.filterNot(::allowCrushing).toMutableList()
    }

    private fun allowCrushing(points: MutableList<Point>): Boolean = points
        .any { it.x < board.size - 1 && board[it.x + 1][it.y].type == null }


    private fun swap() {
        if (swapIndex > 0) {
            when (direction) {
                Direction.RIGHT -> {
                    val firstItem = board[poseI][poseJ + 1]
                    val secondItem = board[poseI][poseJ]
                    board[poseI][poseJ + 1] =
                        firstItem.copy(poseX = firstItem.poseX - cellWidth / 8)
                    board[poseI][poseJ] = secondItem.copy(poseX = secondItem.poseX + cellWidth / 8)
                }

                Direction.LEFT -> {
                    val firstItem = board[poseI][poseJ - 1]
                    val secondItem = board[poseI][poseJ]
                    board[poseI][poseJ - 1] =
                        firstItem.copy(poseX = firstItem.poseX + cellWidth / 8)
                    board[poseI][poseJ] = secondItem.copy(poseX = secondItem.poseX - cellWidth / 8)
                }

                Direction.UP -> {
                    val firstItem = board[poseI - 1][poseJ]
                    val secondItem = board[poseI][poseJ]
                    board[poseI - 1][poseJ] =
                        firstItem.copy(poseY = firstItem.poseY + cellHeight / 8)
                    board[poseI][poseJ] = secondItem.copy(poseY = secondItem.poseY - cellHeight / 8)
                }

                Direction.DOWN -> {
                    val firstItem = board[poseI + 1][poseJ]
                    val secondItem = board[poseI][poseJ]
                    board[poseI + 1][poseJ] =
                        firstItem.copy(poseY = firstItem.poseY - cellHeight / 8)
                    board[poseI][poseJ] = secondItem.copy(poseY = secondItem.poseY + cellHeight / 8)
                }
            }
            swapIndex--
        } else {
            val tmp = board[poseI][poseJ]
            board[poseI][poseJ] = board[newPoseI][newPoseJ]
            board[newPoseI][newPoseJ] = tmp

            board[poseI][poseJ] =
                board[poseI][poseJ].copy(poseX = poseJ * (cellWidth + SPACE_BETWEEN_CELLS_PX))
            board[poseI][poseJ] =
                board[poseI][poseJ].copy(poseY = poseI * (cellHeight + SPACE_BETWEEN_CELLS_PX))
            board[newPoseI][newPoseJ] =
                board[newPoseI][newPoseJ].copy(poseX = newPoseJ * (cellWidth + SPACE_BETWEEN_CELLS_PX))
            board[newPoseI][newPoseJ] =
                board[newPoseI][newPoseJ].copy(poseY = newPoseI * (cellHeight + SPACE_BETWEEN_CELLS_PX))

            swapIndex = 8

            gameState = if (gameState == GameState.SWAPPING) {
                GameState.CHECK_SWAPPING
            } else {
                GameState.NOTHING
            }
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        drawThread.running = true
        drawThread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    var isDestroyed = true

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isDestroyed = false
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(ContextCompat.getColor(context, R.color.ic_launcher_background))
        board.forEachIndexed { i, row ->
            row.forEachIndexed { j, gameItem ->
                canvas.drawBitmap(
                    frameBitmap,
                    (j * (SPACE_BETWEEN_CELLS_PX + cellWidth)).toFloat(),
                    (i * (SPACE_BETWEEN_CELLS_PX + cellHeight)).toFloat(),
                    null
                )
                gameItem.type?.let { itemType ->
                    itemsBitmaps[itemType]?.let { itemBitmap ->
                        canvas.drawBitmap(
                            itemBitmap,
                            gameItem.poseX.toFloat(),
                            gameItem.poseY.toFloat(),
                            null
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val WIDTH = 5
        const val HEIGHT = 6
        const val SPACE_BETWEEN_CELLS_PX = 6
        private const val MIN_DELTA_FOR_DRUGGING_PX = 30
    }
}
