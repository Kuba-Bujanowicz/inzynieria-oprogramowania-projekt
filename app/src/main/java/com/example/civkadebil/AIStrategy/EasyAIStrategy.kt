package com.example.civkadebil.AIStrategy

import android.widget.Toast
import com.example.civkadebil.DataModels.*
import com.example.civkadebil.GameLogic.FieldType
import java.io.Serializable
import kotlin.random.Random

class EasyAIStrategy: AIStrategy, Serializable {

    var treeEn = true
    var branch1 = 0
    var branch2 = 0
    var branch3 = 0
    companion object {
        val INSTANCE = EasyAIStrategy()
    }

    override fun makeMove(gameModel: GameModel) {
        val player = gameModel.currentPlayer
        val units = player?.let { gameModel.findUnit(it.id) }
        val cities= gameModel.cities.filter { it.playerId == player?.id }
        if (units != null) {
        for (unit in units){
        if (cities.isNotEmpty() || unit.type==UnitType.OSADOWNIK) {
            for (city in cities) {
                if (city.cityTurns % 5 == 0) setFighter(city, gameModel)
                if (city.cityTurns % 12 == 0) setSettler(city, gameModel)
                if (city.cityTurns % 6 == 3) setBuilding(city, gameModel)
                if (city.cityTurns % 6 == 2) setZwiadowca(city, gameModel)
            }

            if (units != null) {
                for (unitM in units) {
                    if (unitM.type == UnitType.OSADOWNIK) moveSettler(unitM, gameModel)
                    else if (unitM.unitType == evoType.MELEEFIGHTER) moveMeleeFighter(unitM, gameModel)
                    else if (unitM.unitType == evoType.RANGEFIGHTER) moveRangeFighter(unitM, gameModel)
                    else if (unitM.type == UnitType.ZWIADOWCA) moveZwiadowca(unitM, gameModel)
                }
            }
            if (treeEn) {
                val branch = Random.nextInt(1, 4)
                if (branch == 1) branchOne(branch1, gameModel)
                else if (branch == 2) branchTwo(branch2, gameModel)

            }
            break
        }}}
    }
    override fun enTree(){
        treeEn=true
    }

    fun moveZwiadowca(unit: UnitModel, gameModel: GameModel){
        while (unit.movement[0]!=0) {
            val availableMoves = gameModel.calculateMoveOptions(unit)
            if (availableMoves.isNotEmpty()){
                var randomMove = availableMoves.random()
                if (randomMove.first>0 && randomMove.first<20 && randomMove.second>0 && randomMove.second<40) {
                    gameModel.moveUnit(unit, randomMove.first, randomMove.second)
                }
            }else break
        }
    }

    fun moveSettler(unit: UnitModel, gameModel: GameModel){
        // Zakładanie miasta
        var settlerRow = gameModel.getRowForUnit(unit)
        var settlerCol = gameModel.getColForUnit(unit)
        while (unit.movement[0]!=0) {
            val availableMoves = gameModel.calculateMoveOptions(unit)
            if (availableMoves.isNotEmpty()){
                var randomMove = availableMoves.random()
                if (randomMove.first>0 && randomMove.first<20 && randomMove.second>0 && randomMove.second<40) {
                    gameModel.moveUnit(unit, randomMove.first, randomMove.second)
                    settlerRow = randomMove.first
                    settlerCol = randomMove.second
                }
            }else break
        }
        if (settlerRow>0 && settlerCol>0) {
            if (gameModel.getField(settlerRow, settlerCol).city == null) {
                gameModel.foundCity(settlerRow, settlerCol, "miasto gracza : ${gameModel.currentPlayer?.id}")
                gameModel.setUnit(settlerRow, settlerCol, null)
                gameModel.initUnit(settlerRow, settlerCol + 1, UnitType.WOJOWNIK)
            }
        }
    }
    fun moveMeleeFighter(unit: UnitModel, gameModel: GameModel){
        while (unit.movement[0]!=0) {
            val availableMoves = gameModel.calculateMoveOptions(unit)
            val enemies = gameModel.checkIfEnemyInRange(unit.playerId, availableMoves)
            if (enemies.isNotEmpty()){
                var lowestHalthEnemy = 1000
                var attackUnit: Pair<Int, Int> = enemies.random()
                for (enemy in enemies){
                    val enemyUnit = gameModel.getField(enemy.first,enemy.second).unit

                    if (enemyUnit!!.health < lowestHalthEnemy){
                        lowestHalthEnemy = enemyUnit.health
                        attackUnit = enemy
                    }
                }
                gameModel.moveUnit(unit, attackUnit.first, attackUnit.second)
            }else {
                if (availableMoves.isNotEmpty()){
                    val randomMove = availableMoves.random()
                    gameModel.moveUnit(unit, randomMove.first, randomMove.second)
                }else break

            }
        }
    }
    fun moveRangeFighter(unit: UnitModel, gameModel: GameModel){
        while (unit.movement[0]!=0) {
            val availableMoves = gameModel.calculateMoveOptions(unit)
            val enemies = gameModel.checkIfEnemyInRange(unit.playerId, availableMoves)
            if (enemies.isNotEmpty()){
                var lowestHalthEnemy = 1000
                var attackUnit: Pair<Int, Int> = Pair(0,0)
                for (enemy in enemies){
                    val enemyUnit = gameModel.getField(enemy.first,enemy.second).unit

                    if (enemyUnit?.health!! < lowestHalthEnemy){
                        lowestHalthEnemy = enemyUnit.health
                        attackUnit = enemy
                    }
                }
                gameModel.moveUnit(unit, attackUnit.first, attackUnit.first)
            }else {
                if (availableMoves.isNotEmpty()){
                    val randomMove = availableMoves.random()
                    gameModel.moveUnit(unit, randomMove.first, randomMove.second)
                }else break
            }
        }
    }

    fun setFighter(city: CityModel, gameModel: GameModel){
        val type = Random.nextInt(1, 3)
        val fieldsAva = gameModel.getAdjacentFields(city.centerRow,city.centerCol)
        for (field in fieldsAva){
            if (gameModel.getField(field.first, field.second).unit==null && gameModel.isFieldAllowed(field.first, field.second, listOf(FieldType.LAND))) {
                if (type == 1) {
                    if (gameModel.currentPlayer!!.meleeEvolution[0]==0 && gameModel.currentPlayer!!.gold>=10)
                    {
                        gameModel.initUnit(field.first, field.second, UnitType.WOJOWNIK)
                        gameModel.currentPlayer!!.gold-=10
                    }else if (gameModel.currentPlayer!!.meleeEvolution[0]==1 && gameModel.currentPlayer!!.gold>=25){
                        gameModel.initUnit(field.first, field.second, UnitType.WOJOWNIK)
                        gameModel.currentPlayer!!.gold-=25
                    }else if (gameModel.currentPlayer!!.meleeEvolution[0]==2 && gameModel.currentPlayer!!.gold>=45){
                        gameModel.initUnit(field.first, field.second, UnitType.WOJOWNIK)
                        gameModel.currentPlayer!!.gold-=45
                    }else if (gameModel.currentPlayer!!.meleeEvolution[0]==3 && gameModel.currentPlayer!!.gold>=60){
                        gameModel.initUnit(field.first, field.second, UnitType.WOJOWNIK)
                        gameModel.currentPlayer!!.gold-=60
                    }else if (gameModel.currentPlayer!!.meleeEvolution[0]==4 && gameModel.currentPlayer!!.gold>=80){
                        gameModel.initUnit(field.first, field.second, UnitType.WOJOWNIK)
                        gameModel.currentPlayer!!.gold-=80
                    }

                }
                else if (type == 2) {

                    if (gameModel.currentPlayer!!.rangeEvolution[0]==0 && gameModel.currentPlayer!!.gold>=15)
                    {
                        gameModel.initUnit(field.first, field.second, UnitType.PROCARZ)
                        gameModel.currentPlayer!!.gold-=15
                    }else if (gameModel.currentPlayer!!.rangeEvolution[0]==1 && gameModel.currentPlayer!!.gold>=25){
                        gameModel.initUnit(field.first, field.second, UnitType.PROCARZ)
                        gameModel.currentPlayer!!.gold-=25
                    }else if (gameModel.currentPlayer!!.rangeEvolution[0]==2 && gameModel.currentPlayer!!.gold>=50){
                        gameModel.initUnit(field.first, field.second, UnitType.PROCARZ)
                        gameModel.currentPlayer!!.gold-=50
                    }else if (gameModel.currentPlayer!!.rangeEvolution[0]==3 && gameModel.currentPlayer!!.gold>=60){
                        gameModel.initUnit(field.first, field.second, UnitType.PROCARZ)
                        gameModel.currentPlayer!!.gold-=60
                    }else if (gameModel.currentPlayer!!.rangeEvolution[0]==4 && gameModel.currentPlayer!!.gold>=85){
                        gameModel.initUnit(field.first, field.second, UnitType.PROCARZ)
                        gameModel.currentPlayer!!.gold-=85
                    }
                }
                break
            }
        }

    }

    fun setSettler(city: CityModel, gameModel: GameModel){
        val fieldsAva = gameModel.getAdjacentFields(city.centerRow,city.centerCol)
        for (field in fieldsAva){
            if (gameModel.getField(field.first, field.second).unit==null&& gameModel.isFieldAllowed(field.first, field.second, listOf(FieldType.LAND))&&gameModel.currentPlayer!!.gold>45) {
                gameModel.initUnit(field.first, field.second, UnitType.OSADOWNIK)
                gameModel.currentPlayer!!.gold-=45
                break
            }
        }
    }

    fun setZwiadowca(city: CityModel, gameModel: GameModel){
        val fieldsAva = gameModel.getAdjacentFields(city.centerRow,city.centerCol)
        for (field in fieldsAva){
            if (gameModel.getField(field.first, field.second).unit==null&& gameModel.isFieldAllowed(field.first, field.second, listOf(FieldType.LAND))&&gameModel.currentPlayer!!.gold>15) {
                gameModel.initUnit(field.first, field.second, UnitType.ZWIADOWCA)
                gameModel.currentPlayer!!.gold-=15
                break
            }
        }

    }

    fun setBuilding(city: CityModel, gameModel: GameModel){
        var buildingCords: Pair<Int, Int>
        val type = Random.nextInt(1, 3)

        do {buildingCords = city.cityFields.random()} while (gameModel.getField(buildingCords.first, buildingCords.second).building != null && !gameModel.isFieldAllowed(buildingCords.first, buildingCords.second, listOf(FieldType.LAND)))
        if (type==1 && gameModel.currentPlayer!!.gold>=40) {
            gameModel.initBuilding(buildingCords.first, buildingCords.second, BuildingType.KOSZARY)
            gameModel.currentPlayer!!.gold-=40
        }
        else if (type==2 && gameModel.currentPlayer!!.gold>=30) {
            gameModel.initBuilding(buildingCords.first, buildingCords.second, BuildingType.TARG)
            gameModel.currentPlayer!!.gold-=30
        }
    }

    fun branchOne (lvl:Int, gameModel: GameModel){
        if (lvl==0) {
            gameModel.currentPlayer!!.meleeEvolution[1]=5
            treeEn=false
            branch1++
        }else if (lvl==1) {
            gameModel.currentPlayer!!.meleeEvolution[1]=6
            treeEn=false
            branch1++
        }else if (lvl==2) {
            gameModel.currentPlayer!!.meleeEvolution[1]=7
            treeEn=false
            branch1++
        }else if (lvl==3) {
            gameModel.currentPlayer!!.meleeEvolution[1]=8
            treeEn=false
            branch1++
        }

    }

    fun branchTwo (lvl:Int, gameModel: GameModel){
        if (lvl==0) {
            gameModel.currentPlayer!!.rangeEvolution[1]=5
            treeEn=false
            branch2++
        }else if (lvl==1) {
            gameModel.currentPlayer!!.rangeEvolution[1]=6
            treeEn=false
            branch2++
        }else if (lvl==2) {
            gameModel.currentPlayer!!.rangeEvolution[1]=7
            treeEn=false
            branch2++
        }else if (lvl==3) {
            gameModel.currentPlayer!!.rangeEvolution[1]=8
            treeEn=false
            branch2++
        }

    }
}