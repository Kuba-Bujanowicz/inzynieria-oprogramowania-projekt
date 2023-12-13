package com.example.civkadebil.Graphics

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.civkadebil.DataModels.*


class UnitMenu(private val gameView: GameView, private val gameModel: GameModel) {

    private var currentDialog: AlertDialog? = null

    fun showUnitMenu(unit: UnitModel) {
        val context = gameView.context

        // Tworzenie widoku do menu
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        // Dodawanie TextView dla statystyk jednostki
        val statsTextView = TextView(context)
        statsTextView.text = "GRACZ: ${unit.playerId}\nStatystyki jednostki:\nZdrowie: ${unit.health}\nAtak: ${unit.attack}\nRuch: ${unit.movement[0]}/${unit.movement[1]}"
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
                    gameModel.foundCity(currentRow, currentCol, "chuj")
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
    }

    fun showBuldingMenu(building: BuildingModel) {
        val context = gameView.context

        // Tworzenie widoku do menu
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        // Dodawanie TextView dla statystyk jednostki
        val statsTextView = TextView(context)
        statsTextView.text = "GRACZ: ${building.playerId}\nStatystyki budynku:\nBudynek: ${building.type}\nZdrowie: ${building.health}"
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
        }
    }

    fun showBuldingUnitMenu(building: BuildingModel) {
        val context = gameView.context

        // Tworzenie widoku do menu
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL


        // Tworzenie przycisków dla różnych jednostek
        for (unitType in UnitType.values()) {
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
        }
    }

    fun showBuldingBuildMenu(building: BuildingModel) {
        val context = gameView.context

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
        }
    }

    private fun dismissDialog() {
        currentDialog?.dismiss()
    }

}