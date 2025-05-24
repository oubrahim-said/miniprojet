package com.example.miniprojet.ui.announcements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.miniprojet.R
import com.example.miniprojet.data.model.AnnouncementType
import com.example.miniprojet.databinding.FragmentAnnouncementDetailBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AnnouncementDetailFragment : Fragment() {

    private var _binding: FragmentAnnouncementDetailBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: AnnouncementDetailViewModel
    private var announcementId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnnouncementDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Get announcement ID from arguments
        announcementId = arguments?.getString("announcementId")
        
        if (announcementId == null) {
            // Handle error - no announcement ID provided
            return
        }
        
        // Initialize view model
        viewModel = ViewModelProvider(this)[AnnouncementDetailViewModel::class.java]
        
        // Observe data
        observeData()
        
        // Load data
        viewModel.loadAnnouncementDetails(announcementId!!)
    }
    
    private fun observeData() {
        // Observe announcement details
        viewModel.announcement.observe(viewLifecycleOwner) { announcement ->
            // Update UI with announcement details
            binding.tvAnnouncementTitle.text = announcement.title
            
            // Format date
            val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            binding.tvAnnouncementDate.text = dateFormat.format(announcement.date)
            
            binding.tvAnnouncementAuthor.text = announcement.author
            binding.tvAnnouncementContent.text = announcement.content
            
            // Set announcement type
            binding.tvAnnouncementType.text = announcement.type.name
            
            // Set background color based on announcement type
            val bgColor = when (announcement.type) {
                AnnouncementType.URGENT -> R.color.design_default_color_error
                AnnouncementType.EVENT -> R.color.design_default_color_secondary
                AnnouncementType.MISC -> R.color.design_default_color_primary
            }
            binding.tvAnnouncementType.setBackgroundColor(
                ContextCompat.getColor(requireContext(), bgColor)
            )
        }
        
        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
