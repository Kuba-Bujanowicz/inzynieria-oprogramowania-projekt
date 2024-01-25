package com.example.civkadebil. DataModels

import android.content.Context
import com.example.civkadebil.GameLogic.FieldModel
import com.example.civkadebil.GameLogic.FieldType
import com.example.civkadebil.GameLogic.UnitFactory
import kotlin.random.Random
import android.view.*
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.civkadebil.AIStrategy.EasyAIStrategy
import com.example.civkadebil.DataModels.*
import com.example.civkadebil.EndingScreenActivity
import com.example.civkadebil.EvolutionTreeHandler
import com.example.civkadebil.GameLogic.BuildingFactory
import com.example.civkadebil.MainActivity
import java.io.Serializable


class GameModel : Serializable {
    public val gridSizeX = 20
    public val gridSizeY= 42
    private val barbarians = 4
    private val fields = Array(gridSizeX) { Array(gridSizeY) { FieldModel() } }
    private val rivercount = 4
    private val mountaincount = 5
    var currentTurn: Int = 1
    var currentPlayerTurn: Int = 1
    private var remainingMoves: Int = 0
    val cities: MutableList<CityModel> = mutableListOf()
    var currentPlayer: Player? = null
    val players = mutableListOf<Player>()
    var currentPlayerIndex = 0
    var winner: Player? = null
     lateinit var gameContext: Context
    private val cityStartingSize: Int = 15
    var isNewGame= true
    //val evolutionTreeHandler = currentPlayer?.let { EvolutionTreeHandler(this, it) }


    init {
        // Inicjalizacja mapy
        generateFieldsRandomly()
        generateUnitsRandomly()
    }

fun tree(treeView: View){
    currentPlayer?.evolutionTreeHandler?.setupEvolutionTree(treeView)
}

    fun setGameContext(context: Context, playerAdd:Int, ai:Int) {
        gameContext = context
        if (isNewGame) {
            for (i in 1..playerAdd) {
                initPlayer()
            }
            for (i in 1..ai) {
                initPlayerbot()
            }
            currentPlayer = players[currentPlayerIndex]
            initStartingUnits()
        }
    }

    fun initStartingUnits(){

        for (i in 0 until players.size) {
            var row = Random.nextInt(3, gridSizeX - 4)
            var col = Random.nextInt(3, gridSizeY - 4)
            var x =false
            while (!x) {
                if (fields[row][col].type == FieldType.LAND && checkStartingField(row, col)) {
                    initUnit(row, col, UnitType.OSADOWNIK)
                    x=true
                }
                else {
                    row = Random.nextInt(3, gridSizeX - 4)
                    col = Random.nextInt(3, gridSizeY - 4)
                }
            }
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size
            currentPlayer = players[currentPlayerIndex]
        }
        currentPlayerIndex=0
        currentPlayer = players[currentPlayerIndex]

    }

    fun checkStartingField(row: Int, col: Int): Boolean {
        val movementRange = 3
        for (i in -movementRange..movementRange) {
            for (j in -movementRange..movementRange) {
                val newRow = row + i
                val newCol = col + j

                if (newRow in 0 until gridSizeX && newCol in 0 until gridSizeY &&
                    Math.abs(row - newRow) + Math.abs(col - newCol) <= movementRange && isUnitAt(newRow, newCol)) {
                    // Dodajemy do dostępnych opcji ruchu
                    return false
                }
            }
        }
        return true
    }

    fun startTurn() {
        currentTurn++
        for (player in players) {
            player.income=0
        }
        if (currentTurn%players.size==1) {
            currentPlayerTurn++
            for (city in cities) {
                city.cityTurns++
                for (player in players) {
                    if (city.playerId == player.id) player.income+=city.income
                }
            }
        }
        for (player in players) {
            player.gold+=player.income
        }
        remainingMoves = calculateMaxMovesForTurn()
        if (currentPlayer!!.rangeEvolution[1]>=0) {
            currentPlayer!!.rangeEvolution[1]--
            currentPlayer?.evolutionTreeHandler?.setCoutner(currentPlayer!!.rangeEvolution[1])
        }
        if (currentPlayer!!.meleeEvolution[1]>=0){
            currentPlayer!!.meleeEvolution[1]--
            currentPlayer?.evolutionTreeHandler?.setCoutner(currentPlayer!!.meleeEvolution[1])
        }
        if (currentPlayer!!.vision[1]>=0){
            currentPlayer!!.vision[1]--
            currentPlayer?.evolutionTreeHandler?.setCoutner(currentPlayer!!.vision[1])
        }
        if (currentPlayer!!.rangeEvolution[1]==0){
            currentPlayer!!.rangeEvolution[0]++
            if (!currentPlayer!!.isHuman){
                currentPlayer!!.aiStrategy?.enTree()
            }else currentPlayer?.evolutionTreeHandler?.enableTree()
        }
        if (currentPlayer!!.meleeEvolution[1]==0){
            currentPlayer!!.meleeEvolution[0]++
            if (!currentPlayer!!.isHuman){
                currentPlayer!!.aiStrategy?.enTree()
            } else currentPlayer?.evolutionTreeHandler?.enableTree()
        }
        if (currentPlayer!!.vision[1]==0){
            currentPlayer!!.vision[0]++
            if (!currentPlayer!!.isHuman){
                currentPlayer!!.aiStrategy?.enTree()
            } else currentPlayer?.evolutionTreeHandler?.enableTree()
        }
        if (!currentPlayer?.isHuman!!) {
            currentPlayer?.aiStrategy?.makeMove(this)
            endTurn()
        }
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
        for (i in 0 until gridSizeX) {
            for (j in 0 until gridSizeY) {
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
                size = cityStartingSize,
                cityFields = initCityFields(row, col, cityStartingSize),
                playerId = currentPlayer!!.id,
                cityTurns = 1,
                income = 15
            )

        cities.add(city)
        initBuilding(row,col, BuildingType.RATUSZ)
        initCityFieldsInGameModel(city)
        setUnit(row, col, null)
        revealFogCity(city.centerRow, city.centerCol)

    }

    private fun initCityFields(row: Int, col: Int, size:Int): MutableList<Pair<Int, Int>> {
        // Inicjalizuj podstawowe pola miasta (9 pól)
        val cityFields = mutableListOf<Pair<Int, Int>>()
        for (i in row - 1..row + 1) {
            for (j in col - 1..col + 1) {
                cityFields.add(Pair(i, j))
            }
        }

        var count = 0
        while (count < size-9 ) {

            var fieldNeighbours = getAvailableNeighbors(row, col, 1)
        for (field in cityFields) {
            fieldNeighbours = fieldNeighbours.toMutableList().apply {
                addAll((getAvailableNeighbors(field.first, field.second, 1)))
            }
        }
            val randomIndex = (0 until fieldNeighbours.size).random()
            val randomField = fieldNeighbours[randomIndex]
            if (getField(randomField.first, randomField.second).city == null && getField(randomField.first, randomField.second).type != FieldType.WATER && !cityFields.contains(randomField)) {
                cityFields.add(randomField)
                count++
            }

        }

        // Losuj dodatkowe pola miasta (do 6 pól spośród sąsiadujących)
        /*val availableNeighbors = getAvailableNeighbors(row, col, 2).toMutableList()
        var count = 0

        while (count < size-9 ) {
            val randomIndex = (0 until availableNeighbors.size).random()
            val randomField = availableNeighbors[randomIndex]
            val adjacentFields = getAdjacentFields(randomField.first, randomField.second)
            if(!cityFields.contains(randomField) && (cityFields.contains(adjacentFields[0]) || cityFields.contains(adjacentFields[1]) || cityFields.contains(adjacentFields[2]) || cityFields.contains(adjacentFields[3]))){
                if (randomField.first<gridSizeX && randomField.second<gridSizeY){
                    cityFields.add(randomField)
                    count++
                }

            }
            availableNeighbors.remove(randomField)

        }*/

        return cityFields
    }
    fun calculateDistance(row1: Int, col1: Int, row2: Int, col2: Int): Int {
        // implementacja obliczania odległości Manhattan
        return Math.abs(row1 - row2) + Math.abs(col1 - col2)
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

    fun getAvailableNeighbors(row: Int, col: Int, dist: Int): List<Pair<Int, Int>> {
        val availableNeighbors = mutableListOf<Pair<Int, Int>>()

        for (i in row - dist..row + dist) {
            for (j in col - dist..col + dist) {
                if (i in 0 until gridSizeX && j in 0 until gridSizeY && (i != row || j != col)) {
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
        if (unit != null && unit.type!=UnitType.BARBARZYNCA) {
            revealFog(row, col)
        }
    }

    fun setBuilding(row: Int, col: Int, building: BuildingModel?){
        fields[row][col].building = building
        var city = getField(row,col).city
        if (building!=null){
        if (building.type==BuildingType.TARG) {
            for (player in players){
                if (player.id==building.playerId)
                {
                    player.gold+=5
                    city!!.income+=5
                }
            }
        }
        }
    }

    fun setCity(row: Int, col: Int, city: CityModel?) {
        fields[row][col].city = city
    }
    fun findUnit(playerId: Int): List<UnitModel>{
        val units = mutableListOf<UnitModel>()

        for (j in 0 until gridSizeY) {
            for (i in 0 until gridSizeX) {
                val unit= getField(i,j).unit
                if (unit!= null && unit.playerId == playerId) {
                    units.add(unit)
                }
            }
        }

        return units
    }

    fun addMeleeEvo(player: Player, turnsToEvo: Int){
        player.meleeEvolution[1]= turnsToEvo
    }
    fun addRangeEvo(player: Player, turnsToEvo: Int){
        player.rangeEvolution[1]= turnsToEvo
    }
    fun addThirdEvo(player: Player, turnsToEvo: Int){
        player.vision[1]= turnsToEvo
    }

    fun isUnitAt(row: Int, col: Int): Boolean {
        return getField(row, col).unit != null
    }
    fun isBuildingAt(row: Int, col: Int): Boolean {
        return getField(row, col).building != null
    }

     fun initPlayer() {
         val array: Array<Array<Int>> = Array(gridSizeX) { Array(gridSizeY) { 1 } }
         val player = Player(players.size + 1, true,  EasyAIStrategy.INSTANCE, intArrayOf (0 , -1 ), intArrayOf( 0 ,-1 ), EvolutionTreeHandler(gameContext, this), array, intArrayOf( 2 ,-1 ))
        players.add(player)

         //val evolutionTreeHandlerPlayer = EvolutionTreeHandler(this, player)
    }

    fun initPlayerbot() {
        val array: Array<Array<Int>> = Array(gridSizeX) { Array(gridSizeY) { 1 } }
        val player = Player(players.size + 1, false, EasyAIStrategy.INSTANCE, intArrayOf (0 ,-1 ), intArrayOf (0 ,-1 ), EvolutionTreeHandler(gameContext, this), array, intArrayOf( 2 ,-1 ))
        players.add(player)
    }


    fun initUnit(row: Int, col: Int, unitType: UnitType) {
        var evolvl =0
        if (unitType== UnitType.WOJOWNIK) evolvl= currentPlayer!!.meleeEvolution[0]
        else if (unitType== UnitType.PROCARZ) evolvl= currentPlayer!!.rangeEvolution[0]
        val unit = UnitFactory.createUnit(unitType,  currentPlayer!!.id, evolvl)
        setUnit(row, col, unit)

    }

    fun initBuilding(row: Int, col: Int, buildingType: BuildingType){
        val building = BuildingFactory.createBuilding(buildingType,  currentPlayer!!.id)
        setBuilding(row, col, building)
        var build = getField(row,col).building
        currentPlayer!!.gold-=build!!.cost
    }

    fun revealFog(row: Int, col: Int){
        var fieldsToReveal = calculateFogDistance(row, col, currentPlayer!!.vision[0])

        for (field in fieldsToReveal){
            currentPlayer!!.fog[field.first][field.second]=0
        }
    }

    fun revealFogCity(row: Int, col: Int){
        var city = getField(row,col).city
        for (field in city!!.cityFields){
            currentPlayer!!.fog[field.first][field.second]=0
            var fieldNeighbours = getAvailableNeighbors(field.first,field.second, 1)
            for (fieldN in fieldNeighbours){
                currentPlayer!!.fog[fieldN.first][fieldN.second]=0
            }
        }
    }
    fun getRowForUnit(unit: UnitModel): Int {
        for (i in 0 until gridSizeX) {
            for (j in 0 until gridSizeY) {
                if (getField(i, j).unit == unit) {
                    return i
                }
            }
        }
        return -1 // Zwróć -1, jeśli jednostka nie została znaleziona
    }



    fun getColForUnit(unit: UnitModel): Int {
        for (i in 0 until gridSizeX) {
            for (j in 0 until gridSizeY) {
                if (getField(i, j).unit == unit) {
                    return j
                }
            }
        }
        return -1 // Zwróć -1, jeśli jednostka nie została znaleziona
    }

    fun getColForBuilding(building: BuildingModel): Int {
        for (i in 0 until gridSizeX) {
            for (j in 0 until gridSizeY) {
                if (getField(i, j).building == building) {
                    return j
                }
            }
        }
        return -1 // Zwróć -1, jeśli jednostka nie została znaleziona
    }
    fun getRowForBuilding(building: BuildingModel): Int {
        for (i in 0 until gridSizeX) {
            for (j in 0 until gridSizeY) {
                if (getField(i, j).building == building) {
                    return i
                }
            }
        }
        return -1 // Zwróć -1, jeśli jednostka nie została znaleziona
    }

    private fun generateFieldsRandomly() {
        for (i in 0 until gridSizeX) {
            for (j in 0 until gridSizeY) {
                // Użyj losowej metody generowania pól
                //val randomMountain = Random.nextInt(2, 17)

                if (i == 0 || j == 0 || i == gridSizeX - 1 || j == gridSizeY - 1 || i == 1 || j == 1 || i == gridSizeX - 2 || j == gridSizeY - 2 || j == gridSizeY - 3|| j == gridSizeY - 4) fields[i][j].type =
                    FieldType.WATER

            }
        }

        for (x in 0 until rivercount) {
            val randomRiver = Random.nextInt(2, gridSizeX - 3)
            val randomSide = Random.nextInt(1, 5)
            when (randomSide) {
                1 -> extendRiver(2, randomRiver, 0, 1)
                2 -> extendRiver(randomRiver, 2, 1, 0)
                3 -> extendRiver(randomRiver, gridSizeX - 3, 1, 0)
                else -> extendRiver(gridSizeX - 3, randomRiver, 0, 1)
            }
        }

        var x = 0
        while (x < mountaincount) {
            val randommountainx = Random.nextInt(3, gridSizeX - 4)
            val randommountainy = Random.nextInt(3, gridSizeY - 4)
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

    fun generateUnitsRandomly(){
        var x= 0
        while (x < barbarians) {
            val randombarbarianx = Random.nextInt(3, gridSizeX - 4)
            val randombarbariany = Random.nextInt(3, gridSizeY - 4)
            if (fields[randombarbarianx][randombarbariany].type == FieldType.LAND && fields[randombarbarianx][randombarbariany].unit == null){
                val unit = UnitFactory.createUnit(UnitType.BARBARZYNCA,  -1, 0)
                setUnit(randombarbarianx, randombarbariany, unit)
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
                if (building != null && building.type!=BuildingType.RATUSZ) {
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

        while (x in 0 until gridSizeX-1 && y in 0 until gridSizeY-1) {
            if (fields[x][y].type != FieldType.LAND) {
                break
            }

            fields[x][y].type = FieldType.RIVER

            // Losowo zmień kierunek rzeki
            val changeDirection = Random.nextFloat()
            if (changeDirection < 0.2) {
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


        fun moveUnit(unit: UnitModel, newRow: Int, newCol: Int) {
            val oldRow = getRowForUnit(unit)
            val oldCol = getColForUnit(unit)
            val moveDistance = calculateDistance(oldRow, oldCol, newRow, newCol)


            if ((isUnitAt(newRow, newCol) && getField(newRow,newCol).unit?.playerId !=unit.playerId) || (isBuildingAt(newRow,newCol)&& getField(newRow,newCol).building?.playerId !=unit.playerId)) {
                // Atakuj przeciwną jednostkę
                val targetUnit = getField(newRow, newCol).unit
                val targetBuilding = getField(newRow, newCol).building
                if (targetUnit != null && targetUnit.playerId != unit.playerId) {
                    // Odejmij zdrowie przeciwnikowi
                    targetUnit.health -= unit.attack

                    val moveField = findNearestAttackableField(unit, newRow, newCol)
                    if (moveField != null) {
                        setUnit(oldRow, oldCol, null)
                        setUnit(moveField.first,moveField.second, unit)

                    }
                    unit.movement[0] -=moveDistance
                    // Sprawdź, czy przeciwna jednostka została zniszczona
                    if (targetUnit.health <= 0) {
                        // Przeciwna jednostka została zniszczona, usuń ją z pola
                        if (targetUnit.type==UnitType.BARBARZYNCA) currentPlayer!!.gold+=20
                        else currentPlayer!!.gold+=5
                        setUnit(newRow, newCol, null)
                    }else if (moveField != null) {
                        if (targetUnit.attackRange >= calculateDistance(moveField.first, moveField.second, getRowForUnit(targetUnit), getColForUnit(targetUnit))) {
                            unit.health -= targetUnit.attack
                            if (unit.health<=0) setUnit(moveField.first, moveField.second, null)
                        }
                    }
                }else if (targetBuilding != null && targetBuilding.playerId != unit.playerId) {
                    // Odejmij zdrowie przeciwnikowi
                    targetBuilding.health -= unit.attack

                    val moveField = findNearestAttackableField(unit, newRow, newCol)
                    if (moveField != null) {
                        setUnit(oldRow, oldCol, null)
                        setUnit(moveField.first,moveField.second, unit)
                    }
                    unit.movement[0] -=moveDistance
                    // Sprawdź, czy przeciwna jednostka została zniszczona
                    if (targetBuilding.health <= 0) {
                        // Przeciwna jednostka została zniszczona, usuń ją z pola
                        if (targetBuilding.type== BuildingType.RATUSZ) {
                            destroyCity(newRow, newCol)
                            currentPlayer!!.gold+=30
                            setBuilding(newRow, newCol, null)
                        } else setBuilding(newRow, newCol, null)

                    }
                }
            } else {
                // Przenoś jednostkę na nowe pole

                if (getField(newRow,newCol).unit== null) {
                    setUnit(newRow, newCol, unit)
                    setUnit(oldRow, oldCol, null)
                    val myUnit = getField(newRow, newCol).unit
                    if (myUnit != null) {
                        myUnit.movement[0] -= moveDistance
                    }
                }
            }
        }



    fun findNearestAttackableField(unit: UnitModel, targetRow: Int, targetCol: Int): Pair<Int, Int>? {
        val availableFields = calculateMoveOptions(unit)
        val attackDistance = calculateDistance(getRowForUnit(unit), getColForUnit(unit), targetRow, targetCol)
        if (attackDistance <= unit.attackRange) {
            return Pair(getRowForUnit(unit), getColForUnit(unit))
        }else{
            // Sortuj dostępne pola według odległości od celu
            val sortedFields = availableFields.sortedBy { field ->
                val distance = Math.abs(field.first - getRowForUnit(unit)) + Math.abs(field.second - getColForUnit(unit))
                distance
            }

            // Znajdź pole w zasięgu ataku jednostki
            for (field in sortedFields) {
                if (isWithinAttackRange(unit, field.first, field.second, targetRow, targetCol) && field!= Pair(targetRow, targetCol)) {
                    return field
                }
            }
            return null
        }
    }
    fun isWithinAttackRange(unit: UnitModel, fieldRow: Int, fieldCol: Int, targetRow: Int, targetCol: Int): Boolean {
        val attackDistance = calculateDistance(fieldRow, fieldCol, targetRow, targetCol)
        return attackDistance <= unit.attackRange
    }

    fun calculateMoveOptions(unit: UnitModel): List<Pair<Int, Int>> {
        // Implementuj logikę obliczania dostępnych opcji ruchu dla jednostki
        val options = mutableListOf<Pair<Int, Int>>()

        val currentRow = getRowForUnit(unit)
        val currentCol = getColForUnit(unit)
        val movementRange = unit.movement[0]
        val allowedFieldTypes = unit.fieldTypes

        for (i in -movementRange..movementRange) {
            for (j in -movementRange..movementRange) {
                val newRow = currentRow + i
                val newCol = currentCol + j

                if (newRow >0 && newRow<gridSizeX-2 && newCol>0 && newCol<gridSizeY-2 &&
                    calculateDistance(currentRow, currentCol, newRow, newCol) <= movementRange  &&  isFieldAllowed(newRow, newCol, allowedFieldTypes)) {
                    // Dodajemy do dostępnych opcji ruchu
                    options.add(Pair(newRow, newCol))
                }
            }
        }

        return options
    }

    fun calculateFogDistance(row: Int, col: Int, dist: Int): List<Pair<Int, Int>>{
        val options = mutableListOf<Pair<Int, Int>>()
        for (i in -dist..dist) {
            for (j in -dist..dist) {
                val newRow = row + i
                val newCol = col + j

                if (newRow in 0 until gridSizeX && newCol in 0 until gridSizeY &&
                    calculateDistance(row, col, newRow, newCol) <= dist) {
                    // Dodajemy do dostępnych opcji ruchu
                    options.add(Pair(newRow, newCol))
                }
            }
        }
        return options
    }

    fun checkIfEnemyInRange(unitId: Int, moveOptions: List<Pair<Int, Int>>): List<Pair<Int, Int>>{
        val enemies = mutableListOf<Pair<Int, Int>>()
        for (move in moveOptions){
            if (getField(move.first,move.second).unit != null && getField(move.first,move.second).unit?.playerId!=unitId) enemies.add(move)
        }
        return enemies
    }

    fun isFieldAllowed(row: Int, col: Int, allowedFieldTypes: List<FieldType>): Boolean {
        val field = getField(row, col)
        if (field.type==FieldType.LAND) return true
        else return false
    }

    fun checkEndGameCondition(): Boolean {
        val activePlayers = players.filter { player ->
            // Sprawdź, czy gracz ma co najmniej jednego budynku lub jednostkę
            playerHasBuildingsOrUnits(player)
        }
        for (player in players){
            if (!playerHasBuildingsOrUnits(player)){
                players.remove(player)
            }
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
        for (i in 0 until gridSizeX) {
            for (j in 0 until gridSizeY) {
                val field = getField(i, j)
                if (field.building?.playerId == player.id || (field.unit?.playerId == player.id && field.unit?.type==UnitType.OSADOWNIK)) {
                    return true
                }
            }
        }
        return false
    }
}