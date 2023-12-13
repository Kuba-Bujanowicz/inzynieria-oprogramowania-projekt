package com.example.civkadebil.DataModels

import android.content.Context
import com.example.civkadebil.GameLogic.FieldModel
import com.example.civkadebil.GameLogic.FieldType
import com.example.civkadebil.GameLogic.UnitFactory
import kotlin.random.Random
import android.view.*
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.civkadebil.EndingScreenActivity
import com.example.civkadebil.GameLogic.BuildingFactory

class GameModel {
    private val gridSize = 20
    private val fields = Array(gridSize) { Array(gridSize) { FieldModel() } }
    private val rivercount = 2
    private val mountaincount = 3
    var currentTurn: Int = 1
    private var remainingMoves: Int = 0
    private val cities: MutableList<CityModel> = mutableListOf()
    var currentPlayer: Player? = null
    private val players = mutableListOf<Player>()
    private var currentPlayerIndex = 0
    var winner: Player? = null
    private lateinit var gameContext: Context



    init {
        // Inicjalizacja mapy
        generateFieldsRandomly()
        initPlayer()
        initPlayer()
        currentPlayer = players[currentPlayerIndex]


        initStartingUnits()
    }

    fun setGameContext(context: Context) {
        gameContext = context
    }

    fun initStartingUnits(){
        var row = 6
        var col = 4
        for (i in 0 until players.size) {
            initUnit(row, col, UnitType.SETTLER)
            row+=3
            col+=3
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size
            currentPlayer = players[currentPlayerIndex]
        }
        currentPlayerIndex=0
        currentPlayer = players[currentPlayerIndex]

    }

    fun startTurn() {
        currentTurn++

        remainingMoves = calculateMaxMovesForTurn()
        // Dodaj to tam, gdzie chcesz zaktualizować wartość TextView

    }

    fun endTurn() {
        // Resetuj ruchy jednostek i przejdź do kolejnej tury
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size
        currentPlayer = players[currentPlayerIndex]
        resetMovesForAllUnits()
        if (checkEndGameCondition()) {
            val intent = Intent(gameContext, EndingScreenActivity::class.java)

            // Przekaż ewentualne dodatkowe dane do nowej aktywności
            intent.putExtra("winnerName", winner?.id.toString())

            // Uruchom nową aktywność
            gameContext.startActivity(intent)

            // Zakończ bieżącą aktywność (jeśli chcesz)
        }else startTurn()

         //endingscreen()
    }
    private fun resetMovesForAllUnits() {
        // Resetuj ruchy dla wszystkich jednostek
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                val myUnit = getField(i,j).unit
                if (myUnit != null) {
                    myUnit.movement[0] = myUnit.movement[1]
                }
            }
        }
    }

    fun calculateMaxMovesForTurn(): Int {
        // Możesz dostosować logikę obliczania maksymalnej ilości ruchów w turze
        return 2
    }

    fun foundCity(row: Int, col: Int, cityName: String) {
        val unit = getField(row, col).unit

        val city =
            CityModel(
                name = cityName,
                centerRow = row,
                centerCol = col,
                size = 15,
                cityFields = initCityFields(row, col),
                playerId = currentPlayer!!.id
            )

        cities.add(city)
        initBuilding(row,col, BuildingType.RATUSZ)
        initCityFieldsInGameModel(city)
        setUnit(row, col, null)


    }

    private fun initCityFields(row: Int, col: Int): MutableList<Pair<Int, Int>> {
        // Inicjalizuj podstawowe pola miasta (9 pól)
        val cityFields = mutableListOf<Pair<Int, Int>>()
        for (i in row - 1..row + 1) {
            for (j in col - 1..col + 1) {
                cityFields.add(Pair(i, j))
            }
        }


        // Losuj dodatkowe pola miasta (do 6 pól spośród sąsiadujących)
        val availableNeighbors = getAvailableNeighbors(row, col).toMutableList()
        var count = 0

        while (count < 6 ) {
            val randomIndex = (0 until availableNeighbors.size).random()
            val randomField = availableNeighbors[randomIndex]
            val adjacentFields = getAdjacentFields(randomField.first, randomField.second)
            if(!cityFields.contains(randomField) && (cityFields.contains(adjacentFields[0]) || cityFields.contains(adjacentFields[1]) || cityFields.contains(adjacentFields[2]) || cityFields.contains(adjacentFields[3]))){
                cityFields.add(randomField)
                count++
            }
            availableNeighbors.remove(randomField)

        }

        return cityFields
    }
    fun getAdjacentFields(row: Int, col: Int): List<Pair<Int, Int>> {
        return listOf(
            Pair(row - 1, col),
            Pair(row + 1, col),
            Pair(row, col - 1),
            Pair(row, col + 1)
        )
    }

    fun getCities(list: List<Pair<Int, Int>>): List<Pair<Int, Int>>{
        return list.filter { (row, col) ->
            getField(row, col).city == null
        }
    }

    fun getAvailableNeighbors(row: Int, col: Int): List<Pair<Int, Int>> {
        val availableNeighbors = mutableListOf<Pair<Int, Int>>()

        for (i in row - 2..row + 2) {
            for (j in col - 2..col + 2) {
                if (i in 0 until gridSize && j in 0 until gridSize && (i != row || j != col)) {
                    availableNeighbors.add(Pair(i, j))
                }
            }
        }

        return availableNeighbors
    }

    private fun initCityFieldsInGameModel(city: CityModel) {
        for ((row, col) in city.cityFields) {
            fields[row][col].city = city
        }
    }

    fun calculateMaxMovesForUnit(unit: UnitModel): Int {
        // Możesz dostosować logikę obliczania maksymalnej ilości ruchów dla danej jednostki w turze
        return unit.movement[1]
    }

        fun getField(row: Int, col: Int): FieldModel {
        return fields[row][col]
    }

    fun setFieldType(row: Int, col: Int, fieldType: FieldType) {
        fields[row][col].type = fieldType
    }


    fun setUnit(row: Int, col: Int, unit: UnitModel?) {
        fields[row][col].unit = unit
    }

    fun setBuilding(row: Int, col: Int, building: BuildingModel?){
        fields[row][col].building = building
    }

    fun setCity(row: Int, col: Int, city: CityModel?) {
        fields[row][col].city = city
    }


    fun isUnitAt(row: Int, col: Int): Boolean {
        return getField(row, col).unit != null
    }
    fun isBuildingAt(row: Int, col: Int): Boolean {
        return getField(row, col).building != null
    }

     fun initPlayer() {
        val player = Player(players.size + 1)
        players.add(player)
    }

    fun initUnit(row: Int, col: Int, unitType: UnitType) {
        val unit = UnitFactory.createUnit(unitType,  currentPlayer!!.id)
        setUnit(row, col, unit)
    }
    fun initBuilding(row: Int, col: Int, buildingType: BuildingType){
        val building = BuildingFactory.createBuilding(buildingType,  currentPlayer!!.id)
        setBuilding(row, col, building)
    }

    fun getRowForUnit(unit: UnitModel): Int {
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                if (getField(i, j).unit == unit) {
                    return i
                }
            }
        }
        return -1 // Zwróć -1, jeśli jednostka nie została znaleziona
    }



    fun getColForUnit(unit: UnitModel): Int {
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                if (getField(i, j).unit == unit) {
                    return j
                }
            }
        }
        return -1 // Zwróć -1, jeśli jednostka nie została znaleziona
    }

    fun getColForBuilding(building: BuildingModel): Int {
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                if (getField(i, j).building == building) {
                    return j
                }
            }
        }
        return -1 // Zwróć -1, jeśli jednostka nie została znaleziona
    }
    fun getRowForBuilding(building: BuildingModel): Int {
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                if (getField(i, j).building == building) {
                    return i
                }
            }
        }
        return -1 // Zwróć -1, jeśli jednostka nie została znaleziona
    }

    fun generateFieldsRandomly() {
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                // Użyj losowej metody generowania pól
                val randomMountain = Random.nextInt(2, 17)

                if (i == 0 || j == 0 || i == gridSize - 1 || j == gridSize - 1 || i == 1 || j == 1 || i == gridSize - 2 || j == gridSize - 2) fields[i][j].type =
                    FieldType.WATER


            }
        }

        for (x in 0 until rivercount) {
            val randomRiver = Random.nextInt(2, gridSize - 3)
            val randomSide = Random.nextInt(1, 5)
            when (randomSide) {
                1 -> extendRiver(2, randomRiver, 0, 1)
                2 -> extendRiver(randomRiver, 2, 1, 0)
                3 -> extendRiver(randomRiver, gridSize - 3, 1, 0)
                else -> extendRiver(gridSize - 3, randomRiver, 0, 1)
            }
        }

        var x = 0
        while (x < mountaincount) {
            val randommountainx = Random.nextInt(3, gridSize - 4)
            val randommountainy = Random.nextInt(3, gridSize - 4)
            var randommountainchain = Random.nextInt(1, 4)

            if (fields[randommountainx][randommountainy].type == FieldType.LAND) {
                fields[randommountainx][randommountainy].type = FieldType.MOUNTAIN
                while (randommountainchain>0) {
                    val randommountaindir = Random.nextInt(1, 5)
                    when (randommountaindir) {
                        1 -> if (fields[randommountainx - 1][randommountainy].type == FieldType.LAND) {
                            fields[randommountainx - 1][randommountainy].type = FieldType.MOUNTAIN
                            randommountainchain--
                        }

                        2 -> if (fields[randommountainx + 1][randommountainy].type == FieldType.LAND) {
                            fields[randommountainx + 1][randommountainy].type = FieldType.MOUNTAIN
                            randommountainchain--
                        }

                        3 -> if (fields[randommountainx][randommountainy - 1].type == FieldType.LAND) {
                            fields[randommountainx][randommountainy - 1].type = FieldType.MOUNTAIN
                            randommountainchain--
                        }

                        else -> if (fields[randommountainx][randommountainy + 1].type == FieldType.LAND) {
                            fields[randommountainx][randommountainy + 1].type = FieldType.MOUNTAIN
                            randommountainchain--
                        }
                    }
                }
                x++
            }
        }

    }

    fun destroyCity(row: Int, col: Int) {
        val city = getField(row, col).city

        // Sprawdź, czy miasto istnieje
        if (city != null) {
            // Usuń miasto
            removeCity(city)

            // Usuń wszystkie pola związane z miastem
            city.cityFields.forEach { (cityRow, cityCol) ->
                setCity(cityRow, cityCol, null)
            }

            // Usuń wszystkie budynki związane z miastem
            city.cityFields.forEach { (cityRow, cityCol) ->
                val building = getField(cityRow, cityCol).building
                if (building != null) {
                    setBuilding(cityRow, cityCol, null)
                }
            }
        }
    }

    private fun removeCity(city: CityModel) {
        // Usuń miasto z listy miast
        cities.remove(city)
    }


    private fun extendRiver(startX: Int, startY: Int, dx: Int, dy: Int) {
        var x = startX
        var y = startY
        var dx = dx
        var dy = dy

        while (x in 0 until gridSize && y in 0 until gridSize) {
            if (fields[x][y].type != FieldType.LAND) {
                break
            }

            fields[x][y].type = FieldType.RIVER

            // Losowo zmień kierunek rzeki
            val changeDirection = Random.nextFloat()
            if (changeDirection < 0.4) {
                // Zmień kierunek rzeki
                val newDx = Random.nextInt(-1, 2)
                val newDy = Random.nextInt(-1, 2)
                if (newDx != 0 || newDy != 0) {
                    dx = newDx
                    dy = newDy
                }
            }

            x += dx
            y += dy
        }
    }

    fun checkEndGameCondition(): Boolean {
        val activePlayers = players.filter { player ->
            // Sprawdź, czy gracz ma co najmniej jednego budynku lub jednostkę
            playerHasBuildingsOrUnits(player)
        }

        // Zakończ grę, jeśli został tylko jeden aktywny gracz
        if (activePlayers.size == 1) {
            winner = activePlayers[0] // Ustaw zwycięzcę
            return true
        }

        return false
    }

    private fun playerHasBuildingsOrUnits(player: Player): Boolean {
        // Sprawdź, czy gracz ma co najmniej jednego budynku lub jednostkę
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                val field = getField(i, j)
                if (field.building?.playerId == player.id || field.unit?.playerId == player.id) {
                    return true
                }
            }
        }
        return false
    }
}