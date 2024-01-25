package com.example.civkadebil.ImageHelpers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache

class ImageCache(maxSize: Int) : LruCache<String, Bitmap>(maxSize) {
    override fun sizeOf(key: String, bitmap: Bitmap): Int {
        // Określ, ile miejsca zajmuje bitmapa (w bajtach)
        return bitmap.byteCount
    }
}