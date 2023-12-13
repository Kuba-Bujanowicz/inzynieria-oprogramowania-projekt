package com.example.civkadebil.Graphics

import android.view.ScaleGestureDetector

class ScaleGestureListener(private val gameView: GameView) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
    override fun onScale(detector: ScaleGestureDetector): Boolean {
        gameView.handleScale(detector.scaleFactor)
        return true
    }
}