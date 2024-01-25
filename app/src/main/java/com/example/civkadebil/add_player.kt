package com.example.civkadebil

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class add_player : AppCompatActivity() {

    private lateinit var playerAdapter: PlayerAdapter
    private val players = mutableListOf<addPlayer>()
    var num="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_player)
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(dm)
        MainActivity.GAME_WIDTH = dm.widthPixels
        MainActivity.GAME_HEIGHT = dm.heightPixels
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        val addPlayerButton: Button = findViewById(R.id.addPlayerButton)
        val startGameButton: Button = findViewById(R.id.startGame)

        val playerRecyclerView: RecyclerView = findViewById(R.id.playerRecyclerView)

        playerAdapter = PlayerAdapter(
            players,
            { position -> removePlayerAdd(position) },
            { position, isChecked -> updatePlayerAIStatus(position, isChecked) }
        )
        playerRecyclerView.adapter = playerAdapter
        playerRecyclerView.layoutManager = LinearLayoutManager(this)

        addPlayerButton.setOnClickListener {
            addPlayer()
        }
        startGameButton.setOnClickListener {
            val intent = Intent(this, MainActivity:: class.java)
            var ai=0
            for (player in players){
                if (player.isAI) ai++
            }
            intent.putExtra("playersCount", players.size )
            intent.putExtra("playersAiCount", ai )
            startActivity(intent)
        }
    }

    private fun addPlayer() {

        var numint=1
        numint=num.toInt()+1
        if (numint<=4){
        num=numint.toString()
        val newPlayer = addPlayer("Gracz $num", false)
        players.add(newPlayer)
        playerAdapter.notifyItemInserted(players.size - 1)}
    }

    private fun removePlayerAdd(position: Int) {
        players.removeAt(position)
        playerAdapter.notifyItemRemoved(position)
        var numint=1
        numint=num.toInt()-1
        num=numint.toString()
    }
    private fun updatePlayerAIStatus(position: Int, isChecked: Boolean) {
        val player = players[position]
        player.isAI = isChecked
    }
}