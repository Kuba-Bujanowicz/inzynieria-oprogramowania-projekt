package com.example.civkadebil.GameLogic

import com.example.civkadebil.DataModels.BuildingModel
import com.example.civkadebil.DataModels.BuildingType
import com.example.civkadebil.DataModels.UnitModel
import com.example.civkadebil.DataModels.UnitType

class BuildingFactory {
    companion object {
        fun createBuilding(type: BuildingType,  playerId: Int): BuildingModel {
            return when (type) {
                BuildingType.RATUSZ -> BuildingModel(BuildingType.RATUSZ, 2, 400, playerId)
                BuildingType.KOSZARY -> BuildingModel(BuildingType.KOSZARY, 3, 300, playerId)
                BuildingType.TARG -> BuildingModel(BuildingType.TARG, 1, 100, playerId)
            }
        }
    }
}