package com.example.civkadebil

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.civkadebil.BitmapMethods.Companion.options
import com.example.civkadebil.MainActivity
import com.example.civkadebil.R


enum class Floor(resID: Int, tilesInWidth: Int, tilesInHeight: Int) :
    BitmapMethods {
    OUTSIDE(R.drawable.tileset_floor, 22, 26);

    private val sprites: Array<Bitmap?>

    init {
        options.inScaled = false
        sprites = arrayOfNulls(tilesInHeight * tilesInWidth)
        val spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().resources, resID, options)
        for (j in 0 until tilesInHeight) for (i in 0 until tilesInWidth) {
            val index = j * tilesInWidth + i
            sprites[index] = getScaledBitmap(
                Bitmap.createBitmap(
                    spriteSheet,
                    GameConstants.Sprite.DEFAULT_SIZE * i,
                    GameConstants.Sprite.DEFAULT_SIZE * j,
                    GameConstants.Sprite.DEFAULT_SIZE,
                    GameConstants.Sprite.DEFAULT_SIZE
                )
            )
        }
    }

    fun getSprite(id: Int): Bitmap? {
        return sprites[id]
    }
}