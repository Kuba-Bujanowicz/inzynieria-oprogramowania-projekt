package com.example.civkadebil


import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.civkadebil.DataModels.GameModel
import java.io.Serializable

class EvolutionTreeHandler(private val context: Context, private val gameModel: GameModel): Serializable {


    private var player = gameModel.currentPlayer
    private lateinit var enabledButtons:  MutableList<Button>
    private lateinit var counter: TextView
    private lateinit var textInfo: TextView


    fun disableButton(przycisk: Button){
        przycisk.setBackgroundColor(Color.rgb(66, 66, 66))
        przycisk.isClickable=false
        przycisk.isFocusable=false
        przycisk.isEnabled=false
    }

    fun enableButton(przycisk: Button){
        przycisk.setBackgroundColor(Color.WHITE)
        przycisk.isClickable=true
        przycisk.isFocusable=true
        przycisk.isEnabled=true
    }

    fun disableTree(allButtons: List<List<Button>>) {
        allButtons.forEach { buttonList ->
            buttonList.forEach { button ->
                disableButton(button)
            }
        }
    }

    fun enableTree(){
        enabledButtons.forEach { button ->
            enableButton(button)
        }
        counter.visibility = View.GONE
        textInfo.visibility = View.GONE
    }
    fun setCoutner(number: Int){
        if (gameModel.currentPlayer!!.isHuman) {
            counter.visibility = View.VISIBLE
            textInfo.visibility = View.VISIBLE
            counter.text = number.toString()
        }
    }

    fun setupEvolutionTree(treeView: View) {
        // Zakładam, że masz dostęp do przycisków i innych elementów w widoku drzewka ewolucji
        //currentPlayerId = playerId
        player = gameModel.currentPlayer
        val starozytnosc_1: Button = treeView.findViewById(R.id.starozytnosc_1)
        val starozytnosc_2: Button = treeView.findViewById(R.id.starozytnosc_2)
        val starozytnosc_3: Button = treeView.findViewById(R.id.starozytnosc_3)

        val sredniowiecze_1: Button = treeView.findViewById(R.id.sredniowiecze_1)
        val sredniowiecze_2: Button = treeView.findViewById(R.id.sredniowiecze_2)
        val sredniowiecze_3: Button = treeView.findViewById(R.id.sredniowiecze_3)

        val nowozytnosc_1: Button = treeView.findViewById(R.id.nowozytnosc_1)
        val nowozytnosc_2: Button = treeView.findViewById(R.id.nowozytnosc_2)
        val nowozytnosc_3: Button = treeView.findViewById(R.id.nowozytnosc_3)

        val terazniejszosc_1: Button = treeView.findViewById(R.id.terazniejszosc_1)
        val terazniejszosc_2: Button = treeView.findViewById(R.id.terazniejszosc_2)
        val terazniejszosc_3: Button = treeView.findViewById(R.id.terazniejszosc_3)

        counter = treeView.findViewById(R.id.counter)
        textInfo = treeView.findViewById(R.id.textinfo)

        val meleeButtons = listOf(starozytnosc_1, sredniowiecze_1, nowozytnosc_1, terazniejszosc_1)
        val rangeButtons = listOf(starozytnosc_2, sredniowiecze_2, nowozytnosc_2, terazniejszosc_2)
        val specialButtons = listOf(starozytnosc_3, sredniowiecze_3, nowozytnosc_3, terazniejszosc_3)
        val allButtons= listOf(meleeButtons,rangeButtons,specialButtons)
        if (!::enabledButtons.isInitialized) enabledButtons= mutableListOf(starozytnosc_1,starozytnosc_2, starozytnosc_3)
        // Dodaj inne przyciski, jakie masz w widoku drzewka ewolucji


        starozytnosc_1.setOnClickListener {
            player?.let { it1 -> gameModel.addMeleeEvo(it1, 5) }
            disableTree(allButtons)
            enabledButtons.remove(starozytnosc_1)
            enabledButtons.add(sredniowiecze_1)
            setCoutner(5)
        }

        starozytnosc_2.setOnClickListener {
            player?.let { it1 -> gameModel.addRangeEvo(it1, 5) }
            disableTree(allButtons)
            enabledButtons.remove(starozytnosc_2)
            enabledButtons.add(sredniowiecze_2)
            setCoutner(5)
        }

        starozytnosc_3.setOnClickListener {
            player?.let { it1 -> gameModel.addThirdEvo(it1, 5) }
            disableTree(allButtons)
            enabledButtons.remove(starozytnosc_3)
            enabledButtons.add(sredniowiecze_3)
            setCoutner(5)
        }

        sredniowiecze_1.setOnClickListener {

            player?.let { it1 -> gameModel.addMeleeEvo(it1, 6) }
            disableTree(allButtons)
            enabledButtons.remove(sredniowiecze_1)
            enabledButtons.add(nowozytnosc_1)
            setCoutner(6)
        }

        sredniowiecze_2.setOnClickListener {
            player?.let { it1 -> gameModel.addRangeEvo(it1,6) }
            disableTree(allButtons)
            enabledButtons.remove(sredniowiecze_2)
            enabledButtons.add(nowozytnosc_2)
            setCoutner(6)
        }

        sredniowiecze_3.setOnClickListener {

            disableTree(allButtons)
            enabledButtons.remove(sredniowiecze_3)
            enabledButtons.add(nowozytnosc_3)
            setCoutner(6)
        }


        nowozytnosc_1.setOnClickListener {

            player?.let { it1 -> gameModel.addMeleeEvo(it1, 7) }
            disableTree(allButtons)
            enabledButtons.remove(nowozytnosc_1)
            enabledButtons.add(terazniejszosc_1)
            setCoutner(7)

        }

        nowozytnosc_2.setOnClickListener {

            player?.let { it1 -> gameModel.addRangeEvo(it1, 7) }
            disableTree(allButtons)
            enabledButtons.remove(nowozytnosc_2)
            enabledButtons.add(terazniejszosc_2)
            setCoutner(7)
        }

        nowozytnosc_3.setOnClickListener {

            disableTree(allButtons)
            enabledButtons.remove(nowozytnosc_3)
            enabledButtons.add(terazniejszosc_3)
            setCoutner(7)
        }

        terazniejszosc_1.setOnClickListener {

            player?.let { it1 -> gameModel.addMeleeEvo(it1, 8) }
            disableTree(allButtons)
            enabledButtons.remove(terazniejszosc_1)
            setCoutner(8)
        }

        terazniejszosc_2.setOnClickListener {

            player?.let { it1 -> gameModel.addRangeEvo(it1, 8) }
            disableTree(allButtons)
            enabledButtons.remove(terazniejszosc_2)
            setCoutner(8)
        }

        terazniejszosc_3.setOnClickListener {
            disableTree(allButtons)
            enabledButtons.remove(terazniejszosc_2)
            setCoutner(8)
        }

    }


}