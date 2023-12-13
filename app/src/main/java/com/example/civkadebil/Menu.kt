package com.example.civkadebil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.civkadebil.databinding.ActivityFirstScreenBinding
import com.example.civkadebil.databinding.ActivityMenuBinding

class Menu : AppCompatActivity() {

    lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ButtonNewGame.setOnClickListener{
            val Intent = Intent(this, MainActivity:: class.java)
            startActivity(Intent)
        }
    }
}