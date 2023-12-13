package com.example.civkadebil


import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.Bundle
import android.service.quicksettings.Tile
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import com.example.civkadebil.MainActivity.Companion.GAME_HEIGHT
import com.example.civkadebil.MainActivity.Companion.GAME_WIDTH
import com.tutorial.androidgametutorial.GameLoop
import java.util.*
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

data class Unit(var x: Int, var y: Int)

data class Tile(val x: Int, val y: Int, var color: Int = Color.GRAY)

class Game(context: Context?) : SurfaceView(context), SurfaceHolder.Callback {

    val tileSize = 100
    val mapWidth = 5
    val mapHeight = 5
    val tiles = Array(mapWidth) { x ->
        Array(mapHeight) { y ->
            Tile(x, y)
        }
    }

    val units = mutableListOf<Unit>()
    var activeUnit: Unit? = null

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // Inicjalizacja gry, jeśli to konieczne
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Zmiany w powierzchni, jeśli to konieczne
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Zniszczenie zasobów gry, jeśli to konieczne
    }
    /*private val redPaint = Paint()
    private val holder: SurfaceHolder
    private var x = 0f
    private var y = 0f
    private var movePlayer = false
    private var lastTouchDiff: PointF? = null
    private val rand = Random()
    private val gameLoop: GameLoop
    private val touchEvents: TouchEvents

    private var lastDirChange = System.currentTimeMillis()
    private var playerAniIndexY = 0
    private var aniTick = 0
    private val aniSpeed = 10

    //Testing map
    private val testMap: GameMap

    init {
        holder = getHolder()
        holder.addCallback(this)
        redPaint.color = Color.RED
        touchEvents = TouchEvents(this)
        gameLoop = GameLoop(this)

        val spriteIds = arrayOf(
            intArrayOf(454, 276, 275, 275, 190, 275, 275, 279, 275, 275, 275, 297, 110, 0, 1, 1, 1, 2, 110, 132),
            intArrayOf(454, 275, 169, 232, 238, 275, 275, 275, 276, 275, 275, 297, 110, 22, 89, 23, 23, 24, 110, 132),
            intArrayOf(454, 275, 190, 276, 275, 275, 279, 275, 275, 275, 279, 297, 110, 22, 23, 23, 23, 24, 110, 132),
            intArrayOf(454, 275, 190, 279, 275, 275, 169, 233, 275, 275, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132),
            intArrayOf(454, 275, 190, 276, 277, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 88, 23, 24, 110, 132),
            intArrayOf(454, 275, 235, 232, 232, 232, 260, 279, 276, 279, 275, 297, 110, 22, 23, 89, 23, 24, 110, 132),
            intArrayOf(454, 275, 275, 275, 275, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132),
            intArrayOf(454, 277, 275, 275, 279, 275, 257, 232, 232, 232, 238, 297, 110, 22, 88, 23, 23, 24, 110, 132),
            intArrayOf(454, 275, 275, 275, 275, 275, 190, 279, 275, 275, 275, 297, 110, 22, 23, 23, 88, 24, 110, 132),
            intArrayOf(454, 275, 275, 275, 275, 275, 190, 279, 279, 279, 279, 297, 110, 22, 23, 23, 23, 24, 110, 132),
            intArrayOf(454, 169, 232, 232, 232, 232, 239, 232, 232, 232, 172, 297, 110, 22, 23, 89, 23, 24, 110, 132),
            intArrayOf(454, 190, 279, 275, 275, 275, 275, 275, 275, 275, 190, 297, 110, 44, 45, 45, 45, 46, 110, 132)
        )
        testMap = GameMap(spriteIds)
    }

    fun render() {
        val c = holder.lockCanvas()
        c.drawColor(Color.BLACK)
        testMap.draw(c)


        holder.unlockCanvasAndPost(c)
    }

    fun update(delta: Long) {
        if (System.currentTimeMillis() - lastDirChange >= 3000) {

            lastDirChange = System.currentTimeMillis()
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return true
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        gameLoop.startGameLoop()
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}
    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {}


    */
}