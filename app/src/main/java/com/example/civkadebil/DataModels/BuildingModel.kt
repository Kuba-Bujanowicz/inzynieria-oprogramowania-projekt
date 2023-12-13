package com.example.civkadebil.DataModels

enum class BuildingType {
    RATUSZ,  // Ratusz
    KOSZARY,   // Koszary
    TARG,     // Targ

}

data class BuildingModel(
    val type: BuildingType,
    val buildTime: Int,
    var health: Int,
    var playerId: Int
)
