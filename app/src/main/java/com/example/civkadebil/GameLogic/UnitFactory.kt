package com.example.civkadebil.GameLogic

import com.example.civkadebil.DataModels.UnitModel
import com.example.civkadebil.DataModels.UnitType
import com.example.civkadebil.DataModels.evoType

class UnitFactory {
    companion object {
        fun createUnit(type: UnitType, playerId: Int, evolutionLevel: Int): UnitModel {
            return when (type) {
                UnitType.ZWIADOWCA -> UnitModel(UnitType.ZWIADOWCA, 1,evoType.DIFFERENTUNIT,100, 10, intArrayOf(4, 4), 1, listOf(FieldType.LAND), false, playerId, 15)
                UnitType.WOJOWNIK -> createWarrior(playerId, evolutionLevel)
                UnitType.PROCARZ -> createArcher(playerId, evolutionLevel)
                UnitType.OSADOWNIK -> UnitModel(UnitType.OSADOWNIK, 1,evoType.DIFFERENTUNIT,100, 5, intArrayOf(3, 3), 1, listOf(FieldType.LAND), true, playerId, 45)
                UnitType.BARBARZYNCA -> UnitModel(UnitType.BARBARZYNCA, 1,evoType.DIFFERENTUNIT,100, 10, intArrayOf(2, 2), 1, listOf(FieldType.LAND), false, playerId, 0)
                else -> throw IllegalArgumentException("Unsupported unit type: $type")
            }
        }
        private fun createWarrior(playerId: Int, evolutionLevel: Int): UnitModel{
            return when (evolutionLevel) {
                0 -> {
                    UnitModel(UnitType.WOJOWNIK, evolutionLevel,evoType.MELEEFIGHTER,200, 20, intArrayOf(2, 2), 1, listOf(FieldType.LAND), false, playerId, 10)
                }
                1 -> {
                    UnitModel(UnitType.WLOCZNIK, evolutionLevel,evoType.MELEEFIGHTER,250, 30, intArrayOf(2, 2), 1, listOf(FieldType.LAND), false, playerId, 25)
                }
                2 -> {
                    UnitModel(UnitType.RYCERZ, evolutionLevel,evoType.MELEEFIGHTER,350, 50, intArrayOf(2, 2), 1, listOf(FieldType.LAND), false, playerId, 45)
                }
                3 -> {
                    UnitModel(UnitType.HALABARDZIARZ, evolutionLevel,evoType.MELEEFIGHTER,450, 65, intArrayOf(2, 2), 1, listOf(FieldType.LAND), false, playerId, 60)
                }
                4 -> {
                    UnitModel(UnitType.PIECHOTA, evolutionLevel,evoType.MELEEFIGHTER,600, 85, intArrayOf(3, 3), 1, listOf(FieldType.LAND), false, playerId, 80)
                }
                else -> {
                    UnitModel(UnitType.WOJOWNIK, evolutionLevel,evoType.MELEEFIGHTER,200, 20, intArrayOf(2, 2), 1, listOf(FieldType.LAND), false, playerId, 10)
                }
            }
        }
        private fun createArcher(playerId: Int, evolutionLevel: Int): UnitModel{
            return when (evolutionLevel) {
                0 -> {
                    UnitModel(UnitType.PROCARZ, evolutionLevel, evoType.RANGEFIGHTER,150, 15, intArrayOf(2, 2), 2, listOf(FieldType.LAND), false, playerId, 15)
                }
                1 -> {
                    UnitModel(UnitType.LUCZNIK, evolutionLevel, evoType.RANGEFIGHTER,200, 25, intArrayOf(2, 2), 2, listOf(FieldType.LAND), false, playerId, 25)
                }
                2 -> {
                    UnitModel(UnitType.KUSZNIK, evolutionLevel, evoType.RANGEFIGHTER,250, 40, intArrayOf(2, 2), 2, listOf(FieldType.LAND), false, playerId, 50)
                }
                3 -> {
                    UnitModel(UnitType.ARKEBUZER, evolutionLevel, evoType.RANGEFIGHTER,350, 60, intArrayOf(3, 3), 3, listOf(FieldType.LAND), false, playerId, 60)
                }
                4 -> {
                    UnitModel(UnitType.KARABINIARZ, evolutionLevel, evoType.RANGEFIGHTER,450, 90, intArrayOf(3, 3), 3, listOf(FieldType.LAND), false, playerId, 85)
                }
                else -> {
                    UnitModel(UnitType.PROCARZ, 1, evoType.RANGEFIGHTER,150, 15, intArrayOf(2, 2), 2, listOf(FieldType.LAND), false, playerId, 15)
                }
            }
        }

    }
}