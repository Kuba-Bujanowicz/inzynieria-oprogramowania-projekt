package com.example.civkadebil.Graphics

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.civkadebil.DataModels.*
import com.example.civkadebil.GameLogic.FieldType
import com.example.civkadebil.R


class UnitMenu(private val gameView: GameView, private val gameModel: GameModel) {

    private var currentDialog: AlertDialog? = null

    @SuppressLint("SetTextI18n")
    fun showUnitMenu(unit: UnitModel, unitView: View) {
        /*
        val context = gameView.context

        // Tworzenie widoku do menu
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        // Dodawanie TextView dla statystyk jednostki
        val statsTextView = TextView(context)
        statsTextView.text = "GRACZ: ${unit.playerId}\nStatystyki jednostki:${unit.type}\nZdrowie: ${unit.health}\nAtak: ${unit.attack}\nRuch: ${unit.movement[0]}/${unit.movement[1]}"
        layout.addView(statsTextView)
        if (gameModel.currentPlayer!!.id==unit.playerId) {
            // Dodawanie przycisku "Ruch"
            val moveButton = Button(context)
            moveButton.text = "Ruch"
            moveButton.gravity = Gravity.CENTER

            moveButton.setTextColor(Color.WHITE)
            moveButton.setOnClickListener {
                gameView.isMoveButtonPressed = true
                gameView.showMoveOptions(unit)
                dismissDialog()

            }
            layout.addView(moveButton)

            if (unit.canFoundCity) {

                val cityButton = Button(context)
                cityButton.text = "Załóż miasto"
                cityButton.gravity = Gravity.CENTER
                val currentRow = gameModel.getRowForUnit(unit)
                val currentCol = gameModel.getColForUnit(unit)

                cityButton.setTextColor(Color.WHITE)
                cityButton.setOnClickListener {
                    gameModel.foundCity(currentRow, currentCol, "miasto gracza : ${unit.playerId}")
                    dismissDialog()
                }
                layout.addView(cityButton)
            }
        }
        // Dodawanie przycisku "Wyjdź"
        val exitButton = Button(context)
        exitButton.text = "Wyjdź"
        exitButton.gravity = Gravity.CENTER

        exitButton.setTextColor(Color.WHITE)
        exitButton.setOnClickListener {

            dismissDialog()
        }
        layout.addView(exitButton)




        // Tworzenie AlertDialog
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(layout)
        currentDialog  = dialogBuilder.create()

        // Wyświetlanie dialogu
        currentDialog?.show()
*/
        val unitInfo: TextView = unitView.findViewById(R.id.unitInfo)
        val unitMove: Button = unitView.findViewById(R.id.unitMove)
        val unitWindow: Button = unitView.findViewById(R.id.unitWindow)
        val buildingWindow: Button = unitView.findViewById(R.id.buildingWindow)
        val row = gameModel.getRowForUnit(unit)
        val col = gameModel.getColForUnit(unit)
        unitMove.visibility = if (gameModel.currentPlayer!!.id != unit.playerId) View.GONE else View.VISIBLE
        val unitSettle: Button= unitView.findViewById(R.id.unitSettle)
        unitSettle.visibility = if (gameModel.currentPlayer!!.id == unit.playerId && unit.canFoundCity) View.VISIBLE else View.GONE
        val unitExit: Button = unitView.findViewById(R.id.closeUnitMenu)
        unitInfo.text = "GRACZ: ${unit.playerId}\nStatystyki jednostki:${unit.type}\nZdrowie: ${unit.health}\nAtak: ${unit.attack}\nRuch: ${unit.movement[0]}/${unit.movement[1]}"
        if (gameModel.getField(row,col).building==null) buildingWindow.visibility = View.GONE else buildingWindow.visibility = View.VISIBLE
        unitExit.setOnClickListener {
            gameView.exitUnit()
        }
        unitMove.setOnClickListener {
            gameView.isMoveButtonPressed = true
            gameView.showMoveOptions(unit)
            gameView.exitUnit()
        }
        unitSettle.setOnClickListener{
            val currentRow = gameModel.getRowForUnit(unit)
            val currentCol = gameModel.getColForUnit(unit)
            gameModel.foundCity(currentRow, currentCol, "miasto gracza : ${unit.playerId}")
            gameView.exitUnit()
        }

        buildingWindow.setOnClickListener{
            gameView.exitUnit()
            gameModel.getField(row,col).building?.let { it1 -> gameView.buildingShow(it1) }
        }

    }

    fun showBuldingMenu(building: BuildingModel,player: Player, buildingView: View, buildingAddView: View, unitAddView: View) {
        /*val context = gameView.context
        val row=gameModel.getRowForBuilding(building)
        val col=gameModel.getColForBuilding(building)
        val city = gameModel.getField(row,col).city
        var turydorozbudowy = city?.cityTurns?.rem(3)
        if (turydorozbudowy==0) turydorozbudowy=3
        // Tworzenie widoku do menu
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        // Dodawanie TextView dla statystyk jednostki
        val statsTextView = TextView(context)
        if (building.type==BuildingType.RATUSZ) statsTextView.text = "GRACZ: ${building.playerId}\nStatystyki miasta:\nMelee evo ${player.meleeEvolution[0]}/${player.meleeEvolution[1]}\nRange evo ${player.rangeEvolution[0]}/${player.rangeEvolution[1]}\nNazwa miasta: ${city?.name}\nTury miasta: ${city?.cityTurns}\nWielkość miasta(tury do rozbudowy): ${city?.size}(${turydorozbudowy})\nStatystyki budynku:\nBudynek: ${building.type}\nZdrowie: ${building.health}"
        else statsTextView.text = "GRACZ: ${building.playerId}\nStatystyki budynku:\nBudynek: ${building.type}\nZdrowie: ${building.health}"
        layout.addView(statsTextView)
        if (gameModel.currentPlayer!!.id==building.playerId) {

            // Dodawanie przycisku "budynek"
            val buildButton = Button(context)
            buildButton.text = "Zbuduj budynek"
            buildButton.gravity = Gravity.CENTER

            buildButton.setTextColor(Color.WHITE)
            buildButton.setOnClickListener {

                showBuldingBuildMenu(building)
                dismissDialog()

            }
            layout.addView(buildButton)

            // Dodawanie przycisku "jednostka"
            val unitButton = Button(context)
            unitButton.text = "Zbuduj jednostkę"
            unitButton.gravity = Gravity.CENTER

            unitButton.setTextColor(Color.WHITE)
            unitButton.setOnClickListener {

                showBuldingUnitMenu(building)
                dismissDialog()

            }
            layout.addView(unitButton)
        }
        // Dodawanie przycisku "Wyjdź"
        val exitButton = Button(context)
        exitButton.text = "Wyjdź"
        exitButton.gravity = Gravity.CENTER

        exitButton.setTextColor(Color.WHITE)
        exitButton.setOnClickListener {
            dismissDialog()
        }
        layout.addView(exitButton)

        // Tworzenie AlertDialog
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(layout)
        currentDialog  = dialogBuilder.create()

        // Wyświetlanie dialogu
        gameView.post {
            currentDialog?.show()
        }*/
        val row=gameModel.getRowForBuilding(building)
        val col=gameModel.getColForBuilding(building)
        val city = gameModel.getField(row,col).city
        var turydorozbudowy = city?.cityTurns?.rem(3)
        val unitWindow: Button = buildingView.findViewById(R.id.unitWindow)
        if (turydorozbudowy==0) turydorozbudowy=3
        val buildingInfo: TextView = buildingView.findViewById(R.id.buildingInfo)
        val addUnit: Button = buildingView.findViewById(R.id.addUnit)
        val addBuilding: Button = buildingView.findViewById(R.id.addBuilding)

        val buildingExit: Button = buildingView.findViewById(R.id.closeUnitMenu)
        if (building.type==BuildingType.RATUSZ && building.playerId==gameModel.currentPlayer!!.id) buildingInfo.text = "GRACZ: ${building.playerId}\nStatystyki miasta:\nMelee evo ${player.meleeEvolution[0]}\nRange evo ${player.rangeEvolution[0]}\nTury miasta: ${city?.cityTurns}\nWielkość miasta(tury do rozbudowy): ${city?.size}(${turydorozbudowy})\nZdrowie: ${building.health}"
        else buildingInfo.text = "GRACZ: ${building.playerId}\nStatystyki budynku:\nBudynek: ${building.type}\nZdrowie: ${building.health}"
        if (gameModel.getField(row,col).unit==null) unitWindow.visibility = View.GONE else unitWindow.visibility = View.VISIBLE
        if (building.type==BuildingType.RATUSZ && gameModel.currentPlayer!!.id == building.playerId){
            addUnit.visibility = View.VISIBLE
            addBuilding.visibility = View.VISIBLE
        }else if(gameModel.currentPlayer!!.id != building.playerId){
            addUnit.visibility = View.GONE
            addBuilding.visibility = View.GONE
        }
        buildingExit.setOnClickListener {
            gameView.exitBuilding()
        }
        addUnit.setOnClickListener {
            gameView.exitBuilding()
            gameView.addUnit()
            showBuldingUnitMenu(building, unitAddView)
        }
        addBuilding.setOnClickListener{
            gameView.exitBuilding()
            gameView.addBuilding()
            showBuldingBuildMenu(building, buildingAddView)
        }
        unitWindow.setOnClickListener{
            gameView.exitBuilding()
            gameModel.getField(row,col).unit?.let { it1 -> gameView.unitShow(it1) }
        }

    }

    fun showBuldingUnitMenu(building: BuildingModel, unitAddView: View) {
        /*val context = gameView.context

        // Tworzenie widoku do menu
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL


        // Tworzenie przycisków dla różnych jednostek
        for (unitType in UnitType.values()) {
            if (unitType==UnitType.PROCARZ || unitType==UnitType.WOJOWNIK|| unitType==UnitType.OSADOWNIK||unitType==UnitType.ZWIADOWCA){
            val unitButton = Button(context)
            unitButton.text = "Zbuduj ${unitType.name}"
            unitButton.gravity = Gravity.CENTER

            unitButton.setTextColor(Color.WHITE)
            unitButton.setOnClickListener {
                val row = gameModel.getRowForBuilding(building)
                val col = gameModel.getColForBuilding(building) + 1
                gameModel.initUnit(row, col, unitType)
                dismissDialog()
            }

            layout.addView(unitButton)}
        }

        // Dodawanie przycisku "Wyjdź"
        val exitButton = Button(context)
        exitButton.text = "Wyjdź"
        exitButton.gravity = Gravity.CENTER

        exitButton.setTextColor(Color.WHITE)
        exitButton.setOnClickListener {
            dismissDialog()
        }
        layout.addView(exitButton)

        // Tworzenie AlertDialog
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(layout)
        currentDialog  = dialogBuilder.create()

        // Wyświetlanie dialogu
        gameView.post {
            currentDialog?.show()
        }*/
        val addMelee: Button = unitAddView.findViewById(R.id.addMelee)
        val addRange: Button = unitAddView.findViewById(R.id.addRange)
        var meleeCost= 0
        var rangeCost=0
        when(gameModel.currentPlayer?.meleeEvolution?.get(0)){
            0 -> {
                addMelee.text = "ZBUDUJ WOJOWNIKA 10"
                meleeCost=10
            }
            1 -> {
                addMelee.text = "ZBUDUJ WLOCZNIKA 25"
                meleeCost=25
            }
            2 -> {
                addMelee.text = "ZBUDUJ RYCERZA 45"
                meleeCost=45
            }
            3 -> {
                addMelee.text = "ZBUDUJ HALABARDZIARZA 60"
                meleeCost=60
            }
            4 -> {
                addMelee.text = "ZBUDUJ PIECHOTE 80"
                meleeCost=80
            }
        }

        when(gameModel.currentPlayer?.rangeEvolution?.get(0)){
            0 -> {
                addRange.text = "ZBUDUJ PROCARZA 15"
                rangeCost=15
            }
            1 -> {
                addRange.text = "ZBUDUJ LUCZNIKA 25"
                rangeCost=25
            }
            2 -> {
                addRange.text = "ZBUDUJ KUSZNIKA 50"
                rangeCost=50
            }
            3 -> {
                addRange.text = "ZBUDUJ ARKEBUZERA 60"
                rangeCost=60
            }
            4 -> {
                addRange.text = "ZBUDUJ KARABINIARZA 85"
                rangeCost=85
            }
        }
        val addZWIADOWCA: Button = unitAddView.findViewById(R.id.addZWIADOWCA)
        val addSettler: Button = unitAddView.findViewById(R.id.addSettler)
        val buildingExit: Button = unitAddView.findViewById(R.id.closeUnitMenu)
        if (gameModel.currentPlayer!!.gold<meleeCost) addMelee.setTextColor(ContextCompat.getColor(gameView.context, R.color.red)) else addMelee.setTextColor(ContextCompat.getColor(gameView.context, R.color.black))
        if (gameModel.currentPlayer!!.gold<rangeCost) addRange.setTextColor(ContextCompat.getColor(gameView.context, R.color.red)) else addRange.setTextColor(ContextCompat.getColor(gameView.context, R.color.black))
        if (gameModel.currentPlayer!!.gold<15) addZWIADOWCA.setTextColor(ContextCompat.getColor(gameView.context, R.color.red)) else addZWIADOWCA.setTextColor(ContextCompat.getColor(gameView.context, R.color.black))
        if (gameModel.currentPlayer!!.gold<45) addSettler.setTextColor(ContextCompat.getColor(gameView.context, R.color.red)) else addSettler.setTextColor(ContextCompat.getColor(gameView.context, R.color.black))


        buildingExit.setOnClickListener {
            gameView.exitAddUnit()
        }

        addMelee.setOnClickListener {
            if (gameModel.currentPlayer!!.gold>=meleeCost){
                gameView.exitAddUnit()
                val row = gameModel.getRowForBuilding(building)
                val col = gameModel.getColForBuilding(building)
                if (gameModel.getField(row,col).unit==null&&gameModel.getField(row,col+1).type==FieldType.LAND) {
                    gameModel.initUnit(row, col, UnitType.WOJOWNIK)
                    gameModel.currentPlayer!!.gold -= meleeCost
                }else if (gameModel.getField(row+1,col).unit==null&&gameModel.getField(row+1,col).type==FieldType.LAND) {
                    gameModel.initUnit(row+1, col, UnitType.WOJOWNIK)
                    gameModel.currentPlayer!!.gold -= meleeCost
                }
                else if (gameModel.getField(row,col-1).unit==null&&gameModel.getField(row,col-1).type==FieldType.LAND) {
                    gameModel.initUnit(row, col-1, UnitType.WOJOWNIK)
                    gameModel.currentPlayer!!.gold -= meleeCost
                }
                else if (gameModel.getField(row-1,col).unit==null&&gameModel.getField(row-1,col).type==FieldType.LAND) {
                    gameModel.initUnit(row-1, col, UnitType.WOJOWNIK)
                    gameModel.currentPlayer!!.gold -= meleeCost
                }
                else if (gameModel.getField(row,col+1).unit==null&&gameModel.getField(row,col+1).type==FieldType.LAND) {
                    gameModel.initUnit(row, col+1, UnitType.WOJOWNIK)
                    gameModel.currentPlayer!!.gold -= meleeCost
                }
            }
        }

        addRange.setOnClickListener {
            if (gameModel.currentPlayer!!.gold>=rangeCost){
                gameView.exitAddUnit()
                val row = gameModel.getRowForBuilding(building)
                val col = gameModel.getColForBuilding(building)
                if (gameModel.getField(row,col).unit==null&&gameModel.getField(row,col+1).type==FieldType.LAND) {
                    gameModel.initUnit(row, col, UnitType.PROCARZ)
                    gameModel.currentPlayer!!.gold -= rangeCost
                }else if (gameModel.getField(row+1,col).unit==null&&gameModel.getField(row+1,col).type==FieldType.LAND) {
                    gameModel.initUnit(row+1, col, UnitType.PROCARZ)
                    gameModel.currentPlayer!!.gold -= rangeCost
                }
                else if (gameModel.getField(row,col-1).unit==null&&gameModel.getField(row,col-1).type==FieldType.LAND) {
                    gameModel.initUnit(row, col-1, UnitType.PROCARZ)
                    gameModel.currentPlayer!!.gold -= rangeCost
                }
                else if (gameModel.getField(row-1,col).unit==null&&gameModel.getField(row-1,col).type==FieldType.LAND) {
                    gameModel.initUnit(row-1, col, UnitType.PROCARZ)
                    gameModel.currentPlayer!!.gold -= rangeCost
                }
                else if (gameModel.getField(row,col+1).unit==null&&gameModel.getField(row,col+1).type==FieldType.LAND) {
                    gameModel.initUnit(row, col+1, UnitType.PROCARZ)
                    gameModel.currentPlayer!!.gold -= rangeCost
                }
            }
        }

        addZWIADOWCA.setOnClickListener {
            if (gameModel.currentPlayer!!.gold>=15){
                gameView.exitAddUnit()
                val row = gameModel.getRowForBuilding(building)
                val col = gameModel.getColForBuilding(building)
                if (gameModel.getField(row,col).unit==null&&gameModel.getField(row,col+1).type==FieldType.LAND) {
                    gameModel.initUnit(row, col, UnitType.ZWIADOWCA)
                    gameModel.currentPlayer!!.gold -= 15
                }else if (gameModel.getField(row+1,col).unit==null&&gameModel.getField(row+1,col).type==FieldType.LAND) {
                    gameModel.initUnit(row+1, col, UnitType.ZWIADOWCA)
                    gameModel.currentPlayer!!.gold -= 15
                }
                else if (gameModel.getField(row,col-1).unit==null&&gameModel.getField(row,col-1).type==FieldType.LAND) {
                    gameModel.initUnit(row, col-1, UnitType.ZWIADOWCA)
                    gameModel.currentPlayer!!.gold -= 15
                }
                else if (gameModel.getField(row-1,col).unit==null&&gameModel.getField(row-1,col).type==FieldType.LAND) {
                    gameModel.initUnit(row-1, col, UnitType.ZWIADOWCA)
                    gameModel.currentPlayer!!.gold -= 15
                }
                else if (gameModel.getField(row,col+1).unit==null&&gameModel.getField(row,col+1).type==FieldType.LAND) {
                    gameModel.initUnit(row, col+1, UnitType.ZWIADOWCA)
                    gameModel.currentPlayer!!.gold -= 15
                }
            }
        }

        addSettler.setOnClickListener {
            if (gameModel.currentPlayer!!.gold>=45){
                gameView.exitAddUnit()
                val row = gameModel.getRowForBuilding(building)
                val col = gameModel.getColForBuilding(building)
                if (gameModel.getField(row,col).unit==null&&gameModel.getField(row,col+1).type==FieldType.LAND) {
                    gameModel.initUnit(row, col, UnitType.OSADOWNIK)
                    gameModel.currentPlayer!!.gold -= 45
                }else if (gameModel.getField(row+1,col).unit==null&&gameModel.getField(row+1,col).type==FieldType.LAND) {
                    gameModel.initUnit(row+1, col, UnitType.OSADOWNIK)
                    gameModel.currentPlayer!!.gold -= 45
                }
                else if (gameModel.getField(row,col-1).unit==null&&gameModel.getField(row,col-1).type==FieldType.LAND) {
                    gameModel.initUnit(row, col-1, UnitType.OSADOWNIK)
                    gameModel.currentPlayer!!.gold -= 45
                }
                else if (gameModel.getField(row-1,col).unit==null&&gameModel.getField(row-1,col).type==FieldType.LAND) {
                    gameModel.initUnit(row-1, col, UnitType.OSADOWNIK)
                    gameModel.currentPlayer!!.gold -= 45
                }
                else if (gameModel.getField(row,col+1).unit==null&&gameModel.getField(row,col+1).type==FieldType.LAND) {
                    gameModel.initUnit(row, col+1, UnitType.OSADOWNIK)
                    gameModel.currentPlayer!!.gold -= 45
                }
            }
        }

    }

    fun showBuldingBuildMenu(building: BuildingModel, buildingAddView: View) {
       /* val context = gameView.context

        // Tworzenie widoku do menu
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL


        // Tworzenie przycisków dla różnych jednostek
        for (buildingType in BuildingType.values()) {
            val buildingButton = Button(context)
            buildingButton.text = "Zbuduj ${buildingType.name}"
            buildingButton.gravity = Gravity.CENTER

            buildingButton.setTextColor(Color.WHITE)
            buildingButton.setOnClickListener {
                gameView.selectedBuilding = buildingType
                gameView.isBuildingButtonPressed = true
                dismissDialog()
            }

            layout.addView(buildingButton)
        }

        // Dodawanie przycisku "Wyjdź"
        val exitButton = Button(context)
        exitButton.text = "Wyjdź"
        exitButton.gravity = Gravity.CENTER

        exitButton.setTextColor(Color.WHITE)
        exitButton.setOnClickListener {
            dismissDialog()
        }
        layout.addView(exitButton)

        // Tworzenie AlertDialog
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(layout)
        currentDialog  = dialogBuilder.create()

        // Wyświetlanie dialogu
        gameView.post {
            currentDialog?.show()
        }*/
        val addKoszary: Button = buildingAddView.findViewById(R.id.addKoszary)
        val addTarg: Button = buildingAddView.findViewById(R.id.addTarg)
        val buildingExit: Button = buildingAddView.findViewById(R.id.closeUnitMenu)
        if (gameModel.currentPlayer!!.gold<40) addKoszary.setTextColor(ContextCompat.getColor(gameView.context, R.color.red)) else addKoszary.setTextColor(ContextCompat.getColor(gameView.context, R.color.black))
        if (gameModel.currentPlayer!!.gold<30) addTarg.setTextColor(ContextCompat.getColor(gameView.context, R.color.red)) else addTarg.setTextColor(ContextCompat.getColor(gameView.context, R.color.black))


        buildingExit.setOnClickListener {
            gameView.exitAddBuilding()
        }

        addKoszary.setOnClickListener {
            if (gameModel.currentPlayer!!.gold>=40) {
                gameView.exitAddBuilding()
                gameView.selectedBuilding = BuildingType.KOSZARY
                gameView.isBuildingButtonPressed = true
            }
        }

        addTarg.setOnClickListener {
            if (gameModel.currentPlayer!!.gold>=30) {
                gameView.exitAddBuilding()
                gameView.selectedBuilding = BuildingType.TARG
                gameView.isBuildingButtonPressed = true
            }
        }


    }


}