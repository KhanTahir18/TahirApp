package com.example.tahirapp

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tahirapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // var resultText = findViewById<TextView>(R.id.resText)
       // val userInput1 = binding.editText1
        binding.btnPlus.setOnClickListener {
            val userInput1:String = binding.editText1.text.toString()
            Toast.makeText(this, "You: $userInput1", Toast.LENGTH_SHORT).show()
            val userNum1 = userInput1.toIntOrNull()

            val userInput2:String = binding.editText2.text.toString()
            Toast.makeText(this, "You: $userInput1", Toast.LENGTH_SHORT).show()
            val userNum2 = userInput2.toIntOrNull()

            var addN = userNum1?.plus(userNum2!!)

            var resultText = findViewById<TextView>(R.id.resText)
            resultText.text = addN.toString()
                   }


    }
}