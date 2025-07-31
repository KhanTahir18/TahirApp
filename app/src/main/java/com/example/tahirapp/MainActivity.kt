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
    private lateinit var patientList: ArrayList<Patients>
    private lateinit var adapter: PatientAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()

        adapter = PatientAdapter(patientList)
        binding.recyclerView.adapter = adapter
    }

    private fun initData() {
        patientList = arrayListOf(
            Patients("Tahir", "Has diabetes"),
            Patients("Tahir2", "Has anxiety"),
            Patients("Tahir3", "Flu and fever"),
            Patients("Tahi4", "Back pain"),
            Patients("Tahi5", "Blood pressure"),
            Patients("Tahi6r", "Skin allergy"),
            Patients("Tahi6wer", "Asthma")
        )
    }


}