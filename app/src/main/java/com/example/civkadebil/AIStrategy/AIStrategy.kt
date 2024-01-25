package com.example.civkadebil.AIStrategy

import com.example.civkadebil.DataModels.GameModel

interface AIStrategy {

    fun makeMove(gameModel: GameModel)
    fun enTree()
}