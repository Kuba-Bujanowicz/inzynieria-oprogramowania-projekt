package com.example.civkadebil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.civkadebil.databinding.ActivityFirstScreenBinding
import com.example.civkadebil.databinding.ActivityMenuBinding


class FirstScreen : AppCompatActivity() {

    lateinit var binding : ActivityFirstScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

    binding.startButton.setOnClickListener{
        val Intent = Intent(this, Menu:: class.java)
        startActivity(Intent)
            }
        }

    }
