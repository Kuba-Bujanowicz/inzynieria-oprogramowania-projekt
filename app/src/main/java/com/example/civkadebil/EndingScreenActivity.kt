package com.example.civkadebil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class EndingScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ending_screen)

        val winnerNameTextView: TextView = findViewById(R.id.winnerNameTextView)
        val newGameButton: Button = findViewById(R.id.newGameButton)
        val exitButton: Button = findViewById(R.id.exitButton)

        // Odczytaj nazwę zwycięzcy przekazaną z poprzedniej aktywności
        val winnerName = intent.getStringExtra("winnerName")
        winnerNameTextView.text = "Gracz "+winnerName

        // Obsługa przycisku "Nowa Gra"
        newGameButton.setOnClickListener {
            // Tutaj dodaj kod przenoszący do nowej gry (np. FirstScreenActivity)
            val intent = Intent(this, FirstScreen::class.java)
            startActivity(intent)
            finish() // Zakończ bieżącą aktywność
        }

        // Obsługa przycisku "Wyjdź"
        exitButton.setOnClickListener {
            finishAffinity() // Zakończ wszystkie aktywności w aplikacji
        }
    }
}
