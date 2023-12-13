package com.example.civkadebil

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.widget.Toast


class GameMap(private val spriteIds: Array<IntArray>) {
    fun draw(c: Canvas) {
        for (j in spriteIds.indices) for (i in spriteIds[j].indices) c.drawBitmap(
            Floor.OUTSIDE.getSprite(spriteIds[j][i])!!,
            (i * GameConstants.Sprite.SIZE).toFloat(),
            (j * GameConstants.Sprite.SIZE).toFloat(),
            null
        )
    }
}

class MapView(context: Context, private val game: Game) : View(context) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val tileSize = game.tileSize
        val units = game.units
        val mapWidth = game.mapWidth
        val mapHeight = game.mapHeight
        val tiles = game.tiles

        for (x in 0 until mapWidth) {
            for (y in 0 until mapHeight) {
                val tile = tiles[x][y]
                val paint = android.graphics.Paint()
                paint.color = tile.color
                canvas.drawRect((x * tileSize).toFloat(), (y * tileSize).toFloat(), ((x + 1) * tileSize).toFloat(), ((y + 1) * tileSize).toFloat(), paint)
                paint.color = Color.BLACK
                canvas.drawRect((x * tileSize).toFloat(), (y * tileSize).toFloat(), ((x + 1) * tileSize).toFloat(), ((y + 1) * tileSize).toFloat(), paint)
            }
        }

        // Rysuj jednostki na mapie
        units.forEach { unit ->
            val paint = android.graphics.Paint()
            paint.color = Color.BLUE
            canvas.drawOval((unit.x * tileSize).toFloat(), (unit.y * tileSize).toFloat(), ((unit.x + 1) * tileSize).toFloat(), ((unit.y + 1) * tileSize).toFloat(), paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val clickedX = (event.x / game.tileSize).toInt()
        val clickedY = (event.y / game.tileSize).toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Sprawdź, czy kliknięto na jednostkę, jeśli tak, zaznacz ją jako aktywną
                game.units.forEach { unit ->
                    if (unit.x == clickedX && unit.y == clickedY) {
                        // Zaznacz jednostkę jako aktywną
                        game.activeUnit = unit
                        Toast.makeText(context, "Selected unit at (${unit.x}, ${unit.y})", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                // Jeśli już jest aktywna jednostka, przesuń ją na kliknięte pole
                game.activeUnit?.let { unit ->
                    unit.x = clickedX
                    unit.y = clickedY
                    game.activeUnit = null
                    invalidate()
                }
            }
        }
        return true
    }
}