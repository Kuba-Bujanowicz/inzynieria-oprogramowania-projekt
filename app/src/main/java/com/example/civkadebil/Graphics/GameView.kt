package com.example.civkadebil.Graphics

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.civkadebil.GameLogic.FieldType
import com.example.civkadebil.GameLogic.UnitFactory
import android.app.AlertDialog
import android.view.Gravity
import android.widget.Button
import android.os.Bundle
import com.example.civkadebil.DataModels.*


class GameView(context: Context) : View(context) {

    private val gridSize = 20
    private val squareSize = 150
    private val squareGap = 5

    private val paint = Paint()
    private val units = Array(gridSize) { Array(gridSize) { false } }
    private var selectedUnit: UnitModel? = null

    private val scaleDetector: ScaleGestureDetector = ScaleGestureDetector(context, ScaleGestureListener(this))
    private var scale = 1f
    private var offsetX = 0f
    private var offsetY = 0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var moveOptions: List<Pair<Int, Int>> = emptyList()
    var isMoveButtonPressed = false
    var isBuildingButtonPressed = false
    private var currentDialog: AlertDialog? = null
    private lateinit var gameModel: GameModel
    private lateinit var unitMenu: UnitMenu
    var selectedBuilding: BuildingType = BuildingType.TARG

    init {
        paint.style = Paint.Style.FILL

    }
    fun setGameModel(model: GameModel) {
        gameModel = model
        unitMenu = UnitMenu(this, gameModel)
        gameModel.setGameContext(context)
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val matrix = Matrix()
        matrix.setScale(scale, scale)
        matrix.postTranslate(offsetX, offsetY)
        canvas?.setMatrix(matrix)

        // Rysuj tło
        canvas?.drawColor(Color.BLACK)

        // Rysuj kwadraty
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                val left = j * (squareSize + squareGap).toFloat()
                val top = i * (squareSize + squareGap).toFloat()
                val right = left + squareSize.toFloat()
                val bottom = top + squareSize.toFloat()




                when (gameModel.getField(i, j).type) {
                    FieldType.LAND -> paint.color = Color.GREEN
                    FieldType.MOUNTAIN -> paint.color = Color.GRAY
                    FieldType.WATER -> paint.color = Color.BLUE
                    FieldType.RIVER -> paint.color = Color.CYAN
                }
                val city = gameModel.getField(i, j).city


                canvas?.drawRect(left, top, right, bottom, paint)

                // Narysuj kwadrat


                // Sprawdź, czy to pole jest wśród dostępnych ruchów
                val isMoveOption = moveOptions.contains(Pair(i, j))
                val unit = gameModel.getField(i, j).unit
                val building = gameModel.getField(i, j).building

                // Ustaw kolor ramki na czarny lub odpowiedni kolor dla dostępnych ruchów
                paint.color = if (isMoveOption && unit != null) Color.RED else if (isMoveOption) Color.WHITE else Color.BLACK
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 5f

                // Narysuj ramkę
                canvas?.drawRect(left, top, right, bottom, paint)
                paint.style = Paint.Style.FILL

                if (city != null) {

                    paint.color = Color.DKGRAY
                    canvas?.drawRect(left, top, right, bottom, paint)
                    // Kolor granic miasta
                    val neighBoringCells = gameModel.getAdjacentFields(i,j)
                    val neighboringCities = gameModel.getCities(neighBoringCells)
                    when (city.playerId) {
                        1 -> paint.color = Color.RED
                        2 -> paint.color = Color.BLUE
                        3 -> paint.color = Color.YELLOW
                        4 -> paint.color = Color.GREEN
                    }
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = 5f

                    // Rysuj ramki wokół miasta
                    val borderWidth = 5f // Grubość ramki
                    paint.strokeWidth = borderWidth
                    paint.style = Paint.Style.STROKE

                    // Górna granica miasta
                    if (neighboringCities.contains(Pair(i - 1, j))) {
                        canvas?.drawLine(left, top, right, top, paint)

                    }

                    // Dolna granica miasta
                    if (neighboringCities.contains(Pair(i + 1, j))) {
                        canvas?.drawLine(left, bottom, right, bottom, paint)
                    }

                    // Lewa granica miasta
                    if (neighboringCities.contains(Pair(i, j - 1))) {
                        canvas?.drawLine(left, top, left, bottom, paint)
                    }

                    // Prawa granica miasta
                    if (neighboringCities.contains(Pair(i, j + 1))) {
                        canvas?.drawLine(right, top, right, bottom, paint)
                    }

                    // Przywróć ustawienia malowania
                    paint.style = Paint.Style.FILL
                    paint.strokeWidth = 0f




                }
                if (isMoveOption){
                    paint.color= Color.WHITE
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 5f

                // Narysuj ramkę
                canvas?.drawRect(left, top, right, bottom, paint)
                paint.style = Paint.Style.FILL
                }

                //Narysuj budynek
                if (building != null) {

                    // Rysuj wypełnienie budynku
                    paint.color = Color.GRAY
                    paint.style = Paint.Style.FILL

                    // Dostosuj wielkość i położenie literki w kółku
                    val textX = (left + right) / 2
                    val textY = (top + bottom) / 2
                    val textSize = squareSize / 2f


                    // Kolo z literka odpowiadajaca za typ budynku
                    canvas?.drawCircle(textX, textY, squareSize / 2f, paint)

                    // Ustawienie koloru dla ramki
                    when (building.playerId) {
                        1 -> paint.color = Color.RED
                        2 -> paint.color = Color.BLUE
                        3 -> paint.color = Color.YELLOW
                        4 -> paint.color = Color.GREEN
                    }
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = 5f

                    // Narysuj ramkę wokół koła
                    canvas?.drawCircle(textX, textY, squareSize / 2f, paint)

                    paint.color = Color.BLACK
                    paint.textSize = textSize
                    paint.textAlign = Paint.Align.CENTER

                    // Narysuj literkę odpowiadającą nazwie budynku
                    canvas?.drawText(getBuildingTypeSymbol(building), textX, textY + textSize / 3, paint)

                    paint.textSize = squareSize.toFloat() / 8  // Przywróć rozmiar tekstu do domyślnego
                }


                // Jeśli jednostka jest na polu, narysuj czerwone koło
                if (unit != null) {

                    when (unit.playerId) {
                        1 -> paint.color = Color.RED
                        2 -> paint.color = Color.BLUE
                        3 -> paint.color = Color.YELLOW
                        4 -> paint.color = Color.GREEN
                    }
                    // Dostosuj wielkość i położenie literki w kółku
                    val textX = (left + right) / 2
                    val textY = (top + bottom) / 2
                    val textSize = squareSize / 2f

                    //kolo z literka odpowiadajaca za typ jednostki
                    canvas?.drawCircle(textX, textY, squareSize / 4f, paint)
                     paint.color = Color.BLACK
                     paint.textSize = textSize
                    paint.textAlign = Paint.Align.CENTER
                     canvas?.drawText(getUnitTypeSymbol(unit), textX, textY + textSize / 3, paint)

                    paint.textSize = squareSize.toFloat() / 8  // Przywróć rozmiar tekstu do domyślnego
                }


            }
        }

    }



    override fun onTouchEvent(event: MotionEvent): Boolean {

        scaleDetector.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouch(event.x, event.y)
                lastTouchX = event.x
                lastTouchY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - lastTouchX
                val dy = event.y - lastTouchY


                offsetX += dx / scale
                offsetY += dy / scale



                invalidate()

                lastTouchX = event.x
                lastTouchY = event.y
            }
        }
        scaleDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private fun handleTouch(x: Float, y: Float) {
        val col = ((x - offsetX) / (squareSize + squareGap) / scale).toInt()
        val row = ((y - offsetY) / (squareSize + squareGap) / scale).toInt()

        val clickedUnit = gameModel.getField(row, col).unit

        if (isMoveButtonPressed && selectedUnit != null) {
            if (isValidMove(row, col)) {
                // Przesuwamy jednostkę
                moveUnit(selectedUnit!!, row, col)
            }
            selectedUnit = null
            isMoveButtonPressed = false
            moveOptions = emptyList()
            invalidate()
            dismissDialog()
        }else if (isBuildingButtonPressed ) {
            val clickedcity = gameModel.getField(row, col).city
            if (clickedcity!=null) {

                gameModel.initBuilding(row,col, selectedBuilding)
                isBuildingButtonPressed = false
            }
            selectedUnit = null
            isMoveButtonPressed = false
            moveOptions = emptyList()
            invalidate()
            dismissDialog()
        }else {
            // Sprawdzamy, czy kliknięcie następuje na jednostce
            val clickedUnit = gameModel.getField(row, col).unit
            val clickedBuilding = gameModel.getField(row, col).building
            if (clickedUnit != null) {
                selectedUnit = clickedUnit
                unitMenu.showUnitMenu(selectedUnit!!)
                invalidate()
            }else if (clickedBuilding != null){
                unitMenu.showBuldingMenu(clickedBuilding)
                invalidate()
            }
        }

        // Odśwież widok
        invalidate()
    }

    @SuppressLint("SetTextI18n")



    private fun isValidMove(row: Int, col: Int): Boolean {

        return moveOptions.contains(Pair(row, col))
    }

    private fun moveUnit(unit: UnitModel, newRow: Int, newCol: Int) {
        val oldRow = gameModel.getRowForUnit(unit)
        val oldCol = gameModel.getColForUnit(unit)
        val moveDistance = calculateDistance(oldRow, oldCol, newRow, newCol)


        if (gameModel.isUnitAt(newRow, newCol) || gameModel.isBuildingAt(newRow,newCol)) {
            // Atakuj przeciwną jednostkę
            val targetUnit = gameModel.getField(newRow, newCol).unit
            val targetBuilding = gameModel.getField(newRow, newCol).building
            if (targetUnit != null && targetUnit.playerId != unit.playerId) {
                // Odejmij zdrowie przeciwnikowi
                targetUnit.health -= unit.attack
                unit.movement[0] -=moveDistance
                // Sprawdź, czy przeciwna jednostka została zniszczona
                if (targetUnit.health <= 0) {
                    // Przeciwna jednostka została zniszczona, usuń ją z pola

                    gameModel.setUnit(newRow, newCol, null)
                }
            }else if (targetBuilding != null && targetBuilding.playerId != unit.playerId) {
                // Odejmij zdrowie przeciwnikowi
                targetBuilding.health -= unit.attack
                unit.movement[0] -=moveDistance
                // Sprawdź, czy przeciwna jednostka została zniszczona
                if (targetBuilding.health <= 0) {
                    // Przeciwna jednostka została zniszczona, usuń ją z pola
                    if (targetBuilding.type==BuildingType.RATUSZ) gameModel.destroyCity(newRow, newCol)
                    else gameModel.setBuilding(newRow, newCol, null)
                }
            }
        } else {
            // Przenoś jednostkę na nowe pole
            gameModel.setUnit(newRow, newCol, unit)
            gameModel.setUnit(oldRow, oldCol, null)
            val myUnit = gameModel.getField(newRow,newCol).unit
            if (myUnit != null) {
                myUnit.movement[0] -=moveDistance
            }

        }
    }

    fun showMoveOptions(unit: UnitModel) {

        // Ustalamy dostępne opcje ruchu dla jednostki
        moveOptions = calculateMoveOptions(unit)
        // Tutaj możesz dodatkowo zaznaczyć dostępne opcje ruchu na planszy lub w inny sposób
        invalidate()
    }
    private fun calculateMoveOptions(unit: UnitModel): List<Pair<Int, Int>> {
        // Implementuj logikę obliczania dostępnych opcji ruchu dla jednostki
        val options = mutableListOf<Pair<Int, Int>>()

        val currentRow = gameModel.getRowForUnit(unit)
        val currentCol = gameModel.getColForUnit(unit)
        val movementRange = unit.movement[0]
        val allowedFieldTypes = unit.fieldTypes

        for (i in -movementRange..movementRange) {
            for (j in -movementRange..movementRange) {
                val newRow = currentRow + i
                val newCol = currentCol + j

                if (newRow in 0 until gridSize && newCol in 0 until gridSize &&
                    calculateDistance(currentRow, currentCol, newRow, newCol) <= movementRange  &&  isFieldAllowed(newRow, newCol, allowedFieldTypes)) {
                    // Dodajemy do dostępnych opcji ruchu
                    options.add(Pair(newRow, newCol))
                }
            }
        }

        return options
    }



    private fun isFieldAllowed(row: Int, col: Int, allowedFieldTypes: List<FieldType>): Boolean {
        val field = gameModel.getField(row, col)
        return field.type in allowedFieldTypes
    }
    private fun calculateDistance(row1: Int, col1: Int, row2: Int, col2: Int): Int {
        // implementacja obliczania odległości Manhattan
        return Math.abs(row1 - row2) + Math.abs(col1 - col2)
    }
    private fun isWithinMovementRange(startRow: Int, startCol: Int, targetRow: Int, targetCol: Int, movementRange: Int): Boolean {
        val rowDiff = Math.abs(targetRow - startRow)
        val colDiff = Math.abs(targetCol - startCol)
        return rowDiff + colDiff <= movementRange
    }

    private fun setFieldClickListener(row: Int, col: Int, startRow: Int, startCol: Int) {
        setOnClickListener {
            gameModel.setUnit(row, col, gameModel.getField(startRow, startCol).unit)
            gameModel.setUnit(startRow, startCol, null)
            invalidate()
            // Możesz dodać dodatkowe czynności, np. zamknięcie menu lub odświeżenie widoku
        }
    }

    private fun dismissDialog() {
        currentDialog?.dismiss()
    }
    fun handleScale(scaleFactor: Float) {
        // Obsługa gestów skalowania
        scale *= scaleFactor
        scale = scale.coerceIn(0.1f, 5.0f) // Ogranicz skalę
        invalidate()
    }
    private fun getUnitTypeSymbol(unit: UnitModel): String {
        return when (unit.type) {
            UnitType.FARMER -> "F"
            UnitType.WARRIOR -> "W"
            UnitType.ARCHER -> "A"
            UnitType.SETTLER -> "O"
        }
    }

    private fun getBuildingTypeSymbol(building: BuildingModel): String {
        return when (building.type) {
            BuildingType.KOSZARY -> "K"
            BuildingType.TARG -> "T"
            BuildingType.RATUSZ -> "R"
        }
    }
    private fun darkenColor(color: Int): Int {
        val factor = 0.8f // Mnożnik, który determinuje, jak bardzo ciemnieć kolor (możesz dostosować)
        return Color.argb(
            Color.alpha(color),
            (Color.red(color) * factor).toInt(),
            (Color.green(color) * factor).toInt(),
            (Color.blue(color) * factor).toInt()
        )
    }

    private fun getCityColor(cityName: String): Int {
        // Tutaj możesz zaimplementować logikę przypisującą unikalny kolor dla danego miasta
        // Możesz na przykład użyć jakiejś funkcji mieszającej nazwę miasta, aby uzyskać kolor
        // W tym przykładzie użyto jednej z klasycznych funkcji mieszających
        return cityName.hashCode()
    }

}