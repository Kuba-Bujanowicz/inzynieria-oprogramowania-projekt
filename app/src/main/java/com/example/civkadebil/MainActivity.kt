package com.example.civkadebil

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.graphics.Matrix
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.civkadebil.DataModels.GameModel
import com.example.civkadebil.Graphics.GameView


class MainActivity : AppCompatActivity() {

    companion object {
        var GAME_WIDTH = 0
        var GAME_HEIGHT = 0

        @SuppressLint("StaticFieldLeak")
        private lateinit var gameContext: Context

        fun getGameContext(): Context {
            return gameContext
        }
        private lateinit var gameView: GameView
        private lateinit var gameModel: GameModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameContext = this
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(dm)
        GAME_WIDTH = dm.widthPixels
        GAME_HEIGHT = dm.heightPixels
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        val frameLayout = FrameLayout(this)
       // frameLayout.orientation = frameLayout.VERTICAL


        gameView = GameView(this)
        frameLayout.addView(gameView)

        // Dodaj Warstwę UI
        val uiOverlay = LayoutInflater.from(this).inflate(R.layout.ui_overlay, frameLayout, false)
        frameLayout.addView(uiOverlay)

        setContentView(frameLayout)

        gameModel= GameModel()
        gameView.setGameModel(gameModel)
        val endTurnButton: Button = uiOverlay.findViewById(R.id.endTurnButton)
        val turnsCounterTextView: TextView = uiOverlay.findViewById(R.id.turnsCounter)
        endTurnButton.setOnClickListener {
            gameModel.endTurn()
            turnsCounterTextView.text = gameModel.currentTurn.toString()
        }

    }
}

