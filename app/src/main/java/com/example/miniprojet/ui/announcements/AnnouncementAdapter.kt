package com.example.miniprojet.ui.announcements

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.miniprojet.R
import com.example.miniprojet.data.model.Announcement
import com.example.miniprojet.data.model.AnnouncementType
import com.example.miniprojet.databinding.ItemAnnouncementBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AnnouncementAdapter : ListAdapter<Announcement, AnnouncementAdapter.AnnouncementViewHolder>(AnnouncementDiffCallback()) {

    private var onItemClickListener: ((Announcement) -> Unit)? = null

    fun setOnItemClickListener(listener: (Announcement) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        val binding = ItemAnnouncementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnnouncementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        val announcement = getItem(position)
        holder.bind(announcement)
    }

    inner class AnnouncementViewHolder(private val binding: ItemAnnouncementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                // Use bindingAdapterPosition which is the recommended replacement for adapterPosition
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(getItem(position))
                }
            }
        }

        fun bind(announcement: Announcement) {
            binding.tvAnnouncementTitle.text = announcement.title
            binding.tvAnnouncementContent.text = announcement.content
            binding.tvAnnouncementAuthor.text = "By: ${announcement.author}"

            // Format date
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            binding.tvAnnouncementDate.text = dateFormat.format(announcement.date)

            // Set announcement type
            binding.tvAnnouncementType.text = announcement.type.name

            // Set background color based on announcement type
            val bgColor = when (announcement.type) {
                AnnouncementType.URGENT -> R.color.design_default_color_error
                AnnouncementType.EVENT -> R.color.design_default_color_secondary
                AnnouncementType.MISC -> R.color.design_default_color_primary
            }
            binding.tvAnnouncementType.setBackgroundColor(
                ContextCompat.getColor(binding.root.context, bgColor)
            )
        }
    }

    class AnnouncementDiffCallback : DiffUtil.ItemCallback<Announcement>() {
        override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
            return oldItem == newItem
        }
    }
}
