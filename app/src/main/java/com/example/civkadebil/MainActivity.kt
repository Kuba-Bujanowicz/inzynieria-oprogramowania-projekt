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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.civkadebil.DataModels.GameModel
import com.example.civkadebil.Graphics.GameView
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable


class MainActivity : Serializable, AppCompatActivity() {

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
        private lateinit var frameLayout: FrameLayout
        private lateinit var uiOverlay: View
        private lateinit var unitScreen: View
        private lateinit var buildingScreen: View
        private lateinit var unitShowScreen: View
        private lateinit var buildingShowScreen: View
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
        val gameType = intent.getStringExtra("gameType")
        val playersCount = intent.getIntExtra("playersCount", 1)
        val aiCount = intent.getIntExtra("playersAiCount", 1)
        var humanPlayersCount=playersCount-aiCount

        frameLayout = FrameLayout(this)
       // frameLayout.orientation = frameLayout.VERTICAL
        if (getFileStreamPath("saveGameee.txt").exists() && gameType == "loadGame") {
            val inputStream = openFileInput("saveGameee.txt")
            val objectInputStream = ObjectInputStream(inputStream)
            // Wczytaj instancje gameModel i gameView
            val loadedGameModel = objectInputStream.readObject() as GameModel


            objectInputStream.close()
            inputStream.close()

            // Ustaw wczytane instancje na bieżące
            gameModel = loadedGameModel
            gameModel.isNewGame=false
        }else{

            gameModel= GameModel()
        }
        gameView = GameView(this, this)

        frameLayout.addView(gameView)
        // Dodaj Warstwę UI
        uiOverlay = LayoutInflater.from(this).inflate(R.layout.ui_overlay, frameLayout, false)
        val playerViews = mutableListOf<View>()
        val tree_screen1 = LayoutInflater.from(this).inflate(R.layout.tree_screen, frameLayout, false)
        val tree_screen2 = LayoutInflater.from(this).inflate(R.layout.tree_screen, frameLayout, false)
        val tree_screen3 = LayoutInflater.from(this).inflate(R.layout.tree_screen, frameLayout, false)
        val tree_screen4 = LayoutInflater.from(this).inflate(R.layout.tree_screen, frameLayout, false)
        unitScreen = LayoutInflater.from(this).inflate(R.layout.unit_screen, frameLayout, false)
        buildingScreen = LayoutInflater.from(this).inflate(R.layout.building_screen, frameLayout, false)
        unitShowScreen= LayoutInflater.from(this).inflate(R.layout.addunit_screen, frameLayout, false)
        buildingShowScreen= LayoutInflater.from(this).inflate(R.layout.addbuild_screen, frameLayout, false)
        frameLayout.addView(uiOverlay)

        setContentView(frameLayout)


        gameView.setGameModel(gameModel, unitScreen, buildingScreen, unitShowScreen, buildingShowScreen, humanPlayersCount, aiCount)
        val endTurnButton: Button = uiOverlay.findViewById(R.id.endTurnButton)
        val saveGame:Button = uiOverlay.findViewById(R.id.saveGame)
        val turnsCounterTextView: TextView = uiOverlay.findViewById(R.id.turnsCounter)
        val currentPlayerView: TextView = uiOverlay.findViewById(R.id.currentPlayer)
        val currentPlayerGold: TextView = uiOverlay.findViewById(R.id.gold)
        val treeButton: Button = uiOverlay.findViewById(R.id.treeButton)
        val exitTreeButton1: Button = tree_screen1.findViewById(R.id.closeTree)
        val exitTreeButton2: Button = tree_screen2.findViewById(R.id.closeTree)
        val exitTreeButton3: Button = tree_screen3.findViewById(R.id.closeTree)
        val exitTreeButton4: Button = tree_screen4.findViewById(R.id.closeTree)
        val evolutionTreeHandlers = mutableListOf<EvolutionTreeHandler>()
        val numberOfPlayers = gameModel.players.size
        val evolutionTreeHandler = EvolutionTreeHandler(this, gameModel)
        val player1EvolutionTreeHandler = EvolutionTreeHandler(this, gameModel)
        val player2EvolutionTreeHandler = EvolutionTreeHandler(this, gameModel)
        currentPlayerGold.text="Złoto: "+gameModel.currentPlayer?.gold.toString()+" ("+ gameModel.currentPlayer?.income.toString()+")"
        endTurnButton.setOnClickListener {
            gameModel.endTurn()
            turnsCounterTextView.text = "Tura "+gameModel.currentPlayerTurn.toString()
            currentPlayerView.text = gameModel.currentPlayer?.id.toString()
            currentPlayerGold.text="Złoto: "+gameModel.currentPlayer?.gold.toString()+" ("+ gameModel.currentPlayer?.income.toString()+")"
        }
        treeButton.setOnClickListener{

            frameLayout.removeView(uiOverlay)
            if(gameModel.currentPlayer?.id==1){
            frameLayout.addView(tree_screen1)

            gameModel.tree(tree_screen1)
            } else if (gameModel.currentPlayer?.id==2) {
                frameLayout.addView(tree_screen2)

                gameModel.tree(tree_screen2)
            }else if (gameModel.currentPlayer?.id==3) {
                frameLayout.addView(tree_screen3)

                gameModel.tree(tree_screen3)
            }else if (gameModel.currentPlayer?.id==4) {
                frameLayout.addView(tree_screen4)

                gameModel.tree(tree_screen4)
            }
            setContentView(frameLayout)
        }
        exitTreeButton1.setOnClickListener{

            frameLayout.removeView(tree_screen1)
            frameLayout.addView(uiOverlay)
            setContentView(frameLayout)
        }
        exitTreeButton2.setOnClickListener{

            frameLayout.removeView(tree_screen2)
            frameLayout.addView(uiOverlay)
            setContentView(frameLayout)
        }
        exitTreeButton3.setOnClickListener{

            frameLayout.removeView(tree_screen3)
            frameLayout.addView(uiOverlay)
            setContentView(frameLayout)
        }
        exitTreeButton4.setOnClickListener{

            frameLayout.removeView(tree_screen4)
            frameLayout.addView(uiOverlay)
            setContentView(frameLayout)
        }
        saveGame.setOnClickListener{
                val outputStream = openFileOutput("saveGameee.txt", Context.MODE_PRIVATE)
                val objectOutputStream = ObjectOutputStream(outputStream)

                // Zapisz instancje gameModel i gameView
                objectOutputStream.writeObject(gameModel)
                objectOutputStream.close()
                outputStream.close()

                Toast.makeText(this, "Gra została zapisana", Toast.LENGTH_SHORT).show()

        }
    }

    fun unitShow(){
        frameLayout.removeView(uiOverlay)
        frameLayout.addView(unitScreen)
        setContentView(frameLayout)
    }

    fun unitClose(){
        frameLayout.removeView(unitScreen)
        frameLayout.addView(uiOverlay)
        setContentView(frameLayout)
    }
    fun buildingShow(){
        frameLayout.removeView(uiOverlay)
        frameLayout.addView(buildingScreen)
        setContentView(frameLayout)
    }
    fun buildingClose(){
        frameLayout.removeView(buildingScreen)
        frameLayout.addView(uiOverlay)
        setContentView(frameLayout)
    }

    fun unitSetShow(){
        frameLayout.removeView(uiOverlay)
        frameLayout.addView(unitShowScreen)
        setContentView(frameLayout)
    }

    fun buildingSetShow(){
        frameLayout.removeView(uiOverlay)
        frameLayout.addView(buildingShowScreen)
        setContentView(frameLayout)
    }
    fun unitSetClose(){
        frameLayout.removeView(unitShowScreen)
        frameLayout.addView(uiOverlay)
        setContentView(frameLayout)
    }

    fun buildingSetClose(){
        frameLayout.removeView(buildingShowScreen)
        frameLayout.addView(uiOverlay)
        setContentView(frameLayout)
    }
}

