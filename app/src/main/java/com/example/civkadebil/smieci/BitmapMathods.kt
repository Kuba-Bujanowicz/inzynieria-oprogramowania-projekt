package com.example.civkadebil

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.civkadebil.GameConstants


interface BitmapMethods {
    fun getScaledBitmap(bitmap: Bitmap): Bitmap? {
        return Bitmap.createScaledBitmap(
            bitmap,
            bitmap.width * GameConstants.Sprite.SCALE_MULTIPLIER,
            bitmap.height * GameConstants.Sprite.SCALE_MULTIPLIER,
            false
        )
    }

    companion object {
        val options = BitmapFactory.Options()
    }
}