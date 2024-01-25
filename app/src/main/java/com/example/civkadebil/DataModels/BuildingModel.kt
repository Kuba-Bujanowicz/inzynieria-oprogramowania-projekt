package com.example.civkadebil.DataModels

import java.io.Serializable

enum class BuildingType {
    RATUSZ,  // Ratusz
    KOSZARY,   // Koszary
    TARG,     // Targ

}

data class BuildingModel(
    val type: BuildingType,
    val buildTime: Int,
    var health: Int,
    var playerId: Int,
    var cost: Int
): Serializable
