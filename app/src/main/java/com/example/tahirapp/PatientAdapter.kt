package com.example.tahirapp

// PatientAdapter.kt
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tahirapp.databinding.PatientItemBinding

class PatientAdapter(private val patients: List<Patients>) :
    RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    inner class PatientViewHolder(private val binding: PatientItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(patient: Patients) {
            binding.NameText.text = patient.name
            binding.Infotext.text = patient.info
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = PatientItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(patients[position])
    }

    override fun getItemCount(): Int = patients.size
}
