package com.example.civkadebil.GameLogic

import com.example.civkadebil.DataModels.UnitModel
import com.example.civkadebil.DataModels.UnitType

class UnitFactory {
    companion object {
        fun createUnit(type: UnitType, playerId: Int): UnitModel {
            return when (type) {
                UnitType.FARMER -> UnitModel(UnitType.FARMER, 100, 10, intArrayOf(3, 3), 1, listOf(FieldType.LAND), false, playerId)
                UnitType.WARRIOR -> UnitModel(UnitType.WARRIOR, 200, 20, intArrayOf(2, 2), 1, listOf(FieldType.LAND), false, playerId)
                UnitType.ARCHER -> UnitModel(UnitType.ARCHER, 150, 15, intArrayOf(2, 2), 2, listOf(FieldType.LAND), false, playerId)
                UnitType.SETTLER -> UnitModel(UnitType.SETTLER, 100, 5, intArrayOf(4, 4), 1, listOf(FieldType.LAND), true, playerId)
            }
        }
    }
}