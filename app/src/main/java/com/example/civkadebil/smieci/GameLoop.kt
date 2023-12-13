package com.tutorial.androidgametutorial

import com.example.civkadebil.Game

class GameLoop(private val game: Game) : Runnable {

    private val gameThread: Thread = Thread(this)

    override fun run() {
        var lastFPScheck = System.currentTimeMillis()
        var fps = 0

        var lastDelta = System.nanoTime()
        val nanoSec = 1_000_000_000

        while (true) {
            val nowDelta = System.nanoTime()
            val timeSinceLastDelta = nowDelta - lastDelta
            val delta = timeSinceLastDelta / nanoSec

           // game.update(delta)
           // game.render()
            lastDelta = nowDelta

            fps++

            val now = System.currentTimeMillis()
            if (now - lastFPScheck >= 1000) {
                println("FPS: $fps")
                fps = 0
                lastFPScheck += 1000
            }
        }
    }

    fun startGameLoop() {
        gameThread.start()
    }
}
