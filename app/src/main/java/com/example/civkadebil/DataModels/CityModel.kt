package com.example.civkadebil.DataModels

import java.io.Serializable

data class CityModel(
    val name: String,
    val centerRow: Int,
    val centerCol: Int,
    var size: Int,
    val cityFields: MutableList<Pair<Int, Int>> = mutableListOf(),
    var playerId: Int,
    var cityTurns: Int,
    var income: Int
) : Serializable
