package com.example.miniprojet.ui.courses.exam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.miniprojet.data.remote.ExamSchedule
import com.example.miniprojet.databinding.ItemExamBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ExamAdapter : ListAdapter<ExamSchedule, ExamAdapter.ExamViewHolder>(ExamDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val binding = ItemExamBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val exam = getItem(position)
        holder.bind(exam)
    }

    class ExamViewHolder(private val binding: ItemExamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(exam: ExamSchedule) {
            // Format date
            val dateFormat = SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault())
            binding.tvExamDate.text = dateFormat.format(exam.date)
            
            // Set location
            binding.tvExamLocation.text = exam.location
            
            // Set duration
            binding.tvExamDuration.text = "Duration: ${exam.durationMinutes} minutes"
        }
    }

    class ExamDiffCallback : DiffUtil.ItemCallback<ExamSchedule>() {
        override fun areItemsTheSame(oldItem: ExamSchedule, newItem: ExamSchedule): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExamSchedule, newItem: ExamSchedule): Boolean {
            return oldItem == newItem
        }
    }
}
