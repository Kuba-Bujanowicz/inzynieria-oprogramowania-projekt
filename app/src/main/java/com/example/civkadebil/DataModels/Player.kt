package com.example.civkadebil.DataModels

import com.example.civkadebil.AIStrategy.AIStrategy
import com.example.civkadebil.EvolutionTreeHandler
import java.io.Serializable

data class Player(
    val id: Int,
    val isHuman: Boolean,
    val aiStrategy: AIStrategy? = null,
    var meleeEvolution: IntArray,
    var rangeEvolution: IntArray,
    val evolutionTreeHandler: EvolutionTreeHandler,
    val fog: Array<Array<Int>>,
    var vision: IntArray,
    var gold: Int= 10,
    var income: Int = 0
): Serializable
