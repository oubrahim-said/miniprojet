package com.example.miniprojet.ui.courses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.miniprojet.R
import com.example.miniprojet.data.model.Course
import com.example.miniprojet.databinding.ItemCourseBinding

class CourseAdapter : ListAdapter<Course, CourseAdapter.CourseViewHolder>(CourseDiffCallback()) {

    private var onItemClickListener: ((Course) -> Unit)? = null
    private var onFavoriteClickListener: ((Course, Boolean) -> Unit)? = null

    fun setOnItemClickListener(listener: (Course) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnFavoriteClickListener(listener: (Course, Boolean) -> Unit) {
        onFavoriteClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course)
    }

    inner class CourseViewHolder(private val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(getItem(position))
                }
            }

            binding.ivFavorite.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val course = getItem(position)
                    val newFavoriteState = !course.isFavorite
                    onFavoriteClickListener?.invoke(course, newFavoriteState)
                    
                    // Update the favorite icon
                    updateFavoriteIcon(newFavoriteState)
                }
            }
        }

        fun bind(course: Course) {
            binding.tvCourseTitle.text = course.title
            binding.tvProfessor.text = course.professorName
            binding.tvSchedule.text = course.schedule
            
            // Set favorite icon
            updateFavoriteIcon(course.isFavorite)
        }
        
        private fun updateFavoriteIcon(isFavorite: Boolean) {
            val favoriteIcon = if (isFavorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_favorite_border
            }
            binding.ivFavorite.setImageResource(favoriteIcon)
        }
    }

    class CourseDiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }
    }
}
