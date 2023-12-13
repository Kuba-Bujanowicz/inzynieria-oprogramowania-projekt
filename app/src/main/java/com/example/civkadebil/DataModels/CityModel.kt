package com.example.civkadebil.DataModels

data class CityModel(
    val name: String,
    val centerRow: Int,
    val centerCol: Int,
    var size: Int,
    val cityFields: MutableList<Pair<Int, Int>> = mutableListOf(),
    var playerId: Int
)
