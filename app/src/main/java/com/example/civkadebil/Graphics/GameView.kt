package com.example.civkadebil.Graphics

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.*
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import com.example.civkadebil.DataModels.*
import com.example.civkadebil.GameLogic.FieldType
import com.example.civkadebil.ImageHelpers.ImageManager
import com.example.civkadebil.MainActivity
import com.example.civkadebil.R


class GameView(context: Context, val mainActivity: MainActivity) : View(context) {

    private var gridSizeX = 20
    private var gridSizeY= 40
    private val squareSize = 150
    private val squareGap = 5

    private val paint = Paint()
    private val units = Array(gridSizeX) { Array(gridSizeY) { false } }
    private var selectedUnit: UnitModel? = null
    private lateinit var unitScreen: View
    private lateinit var buildingScreen: View
    private lateinit var unitAddScreen: View
    private lateinit var buildingAddScreen: View
    private lateinit var uiOverlay: View
    private lateinit var frameLayout: FrameLayout
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
    private val imageManager = ImageManager(context)
    var imagePath = R.drawable.grass_15
    var buildingPath = R.drawable.ratusz
    var unitPath = context.resources.getIdentifier("osadnikgreen", "drawable", context.packageName)
    private val screenSize = getScreenSize(context)
    private val screenWidth = screenSize.first
    private val screenHeight = screenSize.second


    init {
        paint.style = Paint.Style.FILL

    }
    fun setGameModel(model: GameModel, unitView: View, buildingView: View, unitAddView: View, buildingAddView: View, players:Int, ai:Int) {
        gameModel = model
        unitMenu = UnitMenu(this, gameModel)
        gameModel.setGameContext(context, players, ai)
        gridSizeX = gameModel.gridSizeX
        gridSizeY= gameModel.gridSizeY
        unitScreen = unitView
        buildingScreen = buildingView
        unitAddScreen = unitAddView
        buildingAddScreen = buildingAddView
    }

    fun scaleBitmap(originalBitmap: Bitmap, scaleRatio: Float): Bitmap {
        val width = Math.round(originalBitmap.width * scaleRatio)
        val height = Math.round(originalBitmap.height * scaleRatio)

        // Utwórz macierz przekształcenia
        val matrix = Matrix()
        matrix.postScale(scaleRatio, scaleRatio)

        // Utwórz nowy przeskalowany obraz
        return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, true)
    }
    @SuppressLint("DrawAllocation", "SuspiciousIndentation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val matrix = Matrix()
        matrix.setScale(scale, scale)
        matrix.postTranslate(offsetX, offsetY)
        canvas?.setMatrix(matrix)

        // Rysuj tło
        canvas?.drawColor(Color.BLACK)

        // Rysuj kwadraty
        for (j in 0 until gridSizeY) {
            for (i in 0 until gridSizeX) {
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
                imagePath = when (gameModel.getField(i, j).type) {
                    FieldType.LAND -> R.drawable.rowniny
                    FieldType.MOUNTAIN -> context.resources.getIdentifier("gora", "drawable", context.packageName)
                    FieldType.WATER -> R.drawable.rzeka
                    FieldType.RIVER -> R.drawable.rzeka
                }
                val city = gameModel.getField(i, j).city
                val building = gameModel.getField(i, j).building
                val unit = gameModel.getField(i, j).unit
                if (building != null) {
                    buildingPath = when (building.type) {
                        BuildingType.RATUSZ -> R.drawable.ratusz
                        BuildingType.KOSZARY -> R.drawable.koszary
                        BuildingType.TARG -> R.drawable.targ
                    }
                }
                var color: String
                color = when (unit?.playerId){
                    1 -> "blue"
                    2 -> "green"
                    3 -> "yellow"
                    4 -> "white"
                    else -> "yellow"
                }
                var bitmapu = imageManager.loadImage(unitPath)
                if (unit != null) {
                    unitPath = when(unit.type){
                        UnitType.WOJOWNIK -> context.resources.getIdentifier("wojownik$color", "drawable", context.packageName)
                        UnitType.WLOCZNIK -> context.resources.getIdentifier("wlocznik$color", "drawable", context.packageName)
                        UnitType.RYCERZ -> context.resources.getIdentifier("rycerz$color", "drawable", context.packageName)
                        UnitType.HALABARDZIARZ -> context.resources.getIdentifier("landsknecht$color", "drawable", context.packageName)
                        UnitType.PIECHOTA -> context.resources.getIdentifier("piechota$color", "drawable", context.packageName)
                        UnitType.PROCARZ -> context.resources.getIdentifier("procarz$color", "drawable", context.packageName)
                        UnitType.LUCZNIK -> context.resources.getIdentifier("lucznik$color", "drawable", context.packageName)
                        UnitType.KUSZNIK -> context.resources.getIdentifier("kusznik$color", "drawable", context.packageName)
                        UnitType.ARKEBUZER -> context.resources.getIdentifier("arkebuzer$color", "drawable", context.packageName)
                        UnitType.KARABINIARZ -> context.resources.getIdentifier("karabin$color", "drawable", context.packageName)
                        UnitType.ZWIADOWCA -> context.resources.getIdentifier("zwiadowca$color", "drawable", context.packageName)
                        UnitType.OSADOWNIK -> context.resources.getIdentifier("osadnik$color", "drawable", context.packageName)
                        UnitType.BARBARZYNCA -> context.resources.getIdentifier("barbazynca", "drawable", context.packageName)
                    }
                    bitmapu = imageManager.loadImage(unitPath)
                }
                var bitmap  = imageManager.loadImage(imagePath)
                val isMoveOption = moveOptions.contains(Pair(i, j))
                if (gameModel.currentPlayer!!.fog[i][j]==0) {
                    //Narysuj kwadrat
                    if (bitmap != null) {
                        // Narysuj bitmapę na płótnie
                        canvas?.drawBitmap(bitmap, left, top, null)

                        //canvas?.drawRect(left, top, right, bottom, paint)
                    } else {
                        // Jeśli obraz nie został załadowany, narysuj coś innego lub poinformuj o błędzie
                        canvas?.drawRect(left, top, right, bottom, paint)
                    }

                    if (city != null) {


                        bitmap = imageManager.loadImage(R.drawable.cien)
                        if (bitmap != null) {
                            canvas?.drawBitmap(bitmap, left, top, null)
                        }

                    }


                    //Narysuj budynek
                    if (building != null) {

                        val bitmapB = imageManager.loadImage(buildingPath)

                        //Narysuj kwadrat
                        if (bitmapB != null) {
                            // Narysuj bitmapę na płótnie
                            canvas?.drawBitmap(bitmapB, left, top, null)
                            //canvas?.drawRect(left, top, right, bottom, paint)
                        } else {

                            canvas?.drawRect(left, top, right, bottom, paint)
                        }
                    }


                    // Jeśli jednostka jest na polu, narysuj  koło
                    if (unit != null) {

                        when (unit.playerId) {
                            -1 -> paint.color = Color.RED
                            1 -> paint.color = Color.BLUE
                            2 -> paint.color = Color.rgb(0, 81, 0)
                            3 -> paint.color = Color.YELLOW
                            4 -> paint.color = Color.WHITE
                        }
                        if (bitmapu != null) {
                            // Narysuj bitmapę na płótnie
                            canvas?.drawBitmap(bitmapu, left, top, null)
                            //canvas?.drawRect(left, top, right, bottom, paint)
                        } else {

                            canvas?.drawRect(left, top, right, bottom, paint)
                        }

                    }
                }else{
                    imagePath=R.drawable.mgla
                    val bitmapF = imageManager.loadImage(imagePath)
                    if (bitmapF != null) {
                        canvas?.drawBitmap(bitmapF, left, top, null)
                    }
                }

                    paint.color= Color.BLACK
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = 5f

                    // Narysuj ramkę
                    canvas?.drawRect(left, top, right, bottom, paint)
                    paint.style = Paint.Style.FILL

                if (city != null&&gameModel.currentPlayer!!.fog[i][j]==0) {


                    // Kolor granic miasta
                    val neighBoringCells = gameModel.getAdjacentFields(i, j)
                    val neighboringCities = gameModel.getCities(neighBoringCells)
                    when (city.playerId) {
                        1 -> paint.color = Color.BLUE
                        2 -> paint.color = Color.rgb(0, 81, 0)
                        3 -> paint.color = Color.YELLOW
                        4 -> paint.color = Color.WHITE
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

                }
                if (isMoveOption && (unit != null && unit.playerId != gameModel.currentPlayerIndex+1 || building!=null && building.playerId != gameModel.currentPlayerIndex+1)){
                    paint.color= Color.RED
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = 5f

                    // Narysuj ramkę
                    canvas?.drawRect(left, top, right, bottom, paint)
                    paint.style = Paint.Style.FILL
                } else if (isMoveOption){
                    paint.color= Color.WHITE
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = 5f

                    // Narysuj ramkę
                    canvas?.drawRect(left, top, right, bottom, paint)
                    paint.style = Paint.Style.FILL
                }
            }
        }

    }



    override fun onTouchEvent(event: MotionEvent): Boolean {

        //scaleDetector.onTouchEvent(event)
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
                val extraWidth = (scale - 1) * 6000f
                val extraHeight = (scale - 1) * 3000f
                val boardWidth = (squareSize+squareGap)* (gridSizeY-4)
                val boardHeight = (squareSize+squareGap)* gridSizeX

                val maxX = (boardWidth * scale - screenWidth).coerceAtLeast(0f)
                val maxY = (boardHeight * scale - screenHeight).coerceAtLeast(0f)

                offsetX += dx * scale
                offsetY += dy * scale
                offsetX = offsetX.coerceIn(-maxX, 0f)
                offsetY = offsetY.coerceIn(-maxY, 0f)



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
                gameModel.moveUnit(selectedUnit!!, row, col)
            }
            selectedUnit = null
            isMoveButtonPressed = false
            moveOptions = emptyList()
            invalidate()
            dismissDialog()
        }else if (isBuildingButtonPressed ) {

            val pole = gameModel.getField(row, col)
            if (pole.city!=null && pole.type==FieldType.LAND && pole.building==null && pole!!.city!!.playerId==gameModel.currentPlayer!!.id) {
                if ((selectedBuilding==BuildingType.TARG && gameModel.currentPlayer!!.gold>=30)|| (selectedBuilding==BuildingType.KOSZARY && gameModel.currentPlayer!!.gold>=40)) {
                    gameModel.initBuilding(row, col, selectedBuilding)
                    isBuildingButtonPressed = false
                }
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
                mainActivity.unitShow()
                unitMenu.showUnitMenu(selectedUnit!!, unitScreen)
                invalidate()
            }else if (clickedBuilding != null){
                mainActivity.buildingShow()
                gameModel.currentPlayer?.let { unitMenu.showBuldingMenu(clickedBuilding, it, buildingScreen, buildingAddScreen, unitAddScreen) }
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

    fun showMoveOptions(unit: UnitModel) {

        // Ustalamy dostępne opcje ruchu dla jednostki
        moveOptions = gameModel.calculateMoveOptions(unit)
        // Tutaj możesz dodatkowo zaznaczyć dostępne opcje ruchu na planszy lub w inny sposób
        invalidate()
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
        scale = scale.coerceIn(0.5f, 2.0f) // Ogranicz skalę
        invalidate()
    }
    private fun getUnitTypeSymbol(unit: UnitModel): String {
        return when (unit.type) {
            UnitType.BARBARZYNCA -> "B"
            UnitType.ZWIADOWCA -> "F"
            UnitType.WOJOWNIK -> "W"
            UnitType.PROCARZ -> "A"
            UnitType.OSADOWNIK -> "O"
            UnitType.WLOCZNIK -> "W"
            UnitType.LUCZNIK -> "L"
            else -> throw IllegalArgumentException("Unsupported unit type: ")
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

    fun getScreenSize(context: Context): Pair<Int, Int> {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()

        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        return Pair(screenWidth, screenHeight)
    }

    fun unitShow(unit: UnitModel){
        mainActivity.unitShow()
        unitMenu.showUnitMenu(unit, unitScreen)
    }

    fun buildingShow(building: BuildingModel){
        mainActivity.buildingShow()
        gameModel.currentPlayer?.let { unitMenu.showBuldingMenu(building, it, buildingScreen, buildingAddScreen, unitAddScreen) }
    }

    fun exitUnit(){
        mainActivity.unitClose()
    }
    fun exitBuilding(){
        mainActivity.buildingClose()
    }

    fun addUnit(){
        mainActivity.unitSetShow()
    }
    fun addBuilding(){
        mainActivity.buildingSetShow()
    }

    fun exitAddUnit(){
        mainActivity.unitSetClose()
    }
    fun exitAddBuilding(){
        mainActivity.buildingSetClose()
    }

}