package com.example.civkadebil.GameLogic

import com.example.civkadebil.DataModels.BuildingModel
import com.example.civkadebil.DataModels.CityModel
import com.example.civkadebil.DataModels.UnitModel
import java.io.Serializable

data class FieldModel(

    var type: FieldType = FieldType.LAND,
    var unit: UnitModel? = null,
    var building: BuildingModel? = null,
    var city: CityModel? = null
) : Serializable
