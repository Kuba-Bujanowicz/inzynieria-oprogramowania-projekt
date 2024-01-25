package com.example.civkadebil.ImageHelpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache

class ImageManager(private val context: Context) {
    private val imageCache: LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        imageCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }
    }

    fun loadImage(resourceId: Int): Bitmap? {
        val key = resourceId.toString()
        val cachedBitmap = imageCache.get(key)
        if (cachedBitmap != null) {
            return cachedBitmap
        } else {
            var bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
            bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false)
            imageCache.put(key, bitmap)
            return bitmap
        }
    }

    // Dodaj inne metody zarządzania obrazami, jeśli to konieczne
}