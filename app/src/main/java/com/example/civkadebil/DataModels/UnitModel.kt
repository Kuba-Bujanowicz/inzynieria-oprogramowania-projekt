package com.example.civkadebil.DataModels

import com.example.civkadebil.GameLogic.FieldType

enum class UnitType {
    FARMER,
    WARRIOR,
    ARCHER,
    SETTLER
}

data class UnitModel(
    var type: UnitType,
    var health: Int,
    var attack: Int,
    var movement: IntArray,
    var attackRange: Int,
    var fieldTypes: List<FieldType>,
    var canFoundCity: Boolean,
    var playerId: Int
)