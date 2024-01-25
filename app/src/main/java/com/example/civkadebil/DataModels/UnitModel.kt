package com.example.civkadebil.DataModels

import com.example.civkadebil.GameLogic.FieldType
import java.io.Serializable


enum class UnitType(val evolutionLevel: Int) {

    //melee
    WOJOWNIK(0),
    WLOCZNIK(1),
    RYCERZ(2),
    HALABARDZIARZ(3),
    PIECHOTA(4),

    //ranged
    PROCARZ(0),
    LUCZNIK(1),
    KUSZNIK(2),
    ARKEBUZER(3),
    KARABINIARZ(4),

    //inne jednostki
    ZWIADOWCA(0),
    OSADOWNIK(0),
    BARBARZYNCA(0);


}

enum class evoType(){
    MELEEFIGHTER,
    RANGEFIGHTER,
    DIFFERENTUNIT
}

data class UnitModel(
    var type: UnitType,
    var evolutionLevel: Int,
    var unitType: evoType,
    var health: Int,
    var attack: Int,
    var movement: IntArray,
    var attackRange: Int,
    var fieldTypes: List<FieldType>,
    var canFoundCity: Boolean,
    var playerId: Int,
    var cost: Int
): Serializable
