package com.example.miniprojet.ui.announcements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.miniprojet.R
import com.example.miniprojet.data.model.AnnouncementType
import com.example.miniprojet.databinding.FragmentAnnouncementListBinding

class AnnouncementListFragment : Fragment() {

    companion object {
        private const val ARG_TYPE = "type"
        
        fun newInstance(type: AnnouncementType): AnnouncementListFragment {
            return AnnouncementListFragment().apply {
                arguments = bundleOf(ARG_TYPE to type.name)
            }
        }
    }
    
    private var _binding: FragmentAnnouncementListBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: AnnouncementListViewModel
    private lateinit var announcementAdapter: AnnouncementAdapter
    private lateinit var announcementType: AnnouncementType

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnnouncementListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Get announcement type from arguments
        val typeName = arguments?.getString(ARG_TYPE) ?: AnnouncementType.MISC.name
        announcementType = AnnouncementType.valueOf(typeName)
        
        // Initialize view model
        viewModel = ViewModelProvider(this)[AnnouncementListViewModel::class.java]
        
        // Set up recycler view
        setupRecyclerView()
        
        // Set up swipe refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshAnnouncements()
        }
        
        // Observe data
        observeData()
        
        // Load data
        viewModel.loadAnnouncements(announcementType)
    }
    
    private fun setupRecyclerView() {
        announcementAdapter = AnnouncementAdapter().apply {
            setOnItemClickListener { announcement ->
                // Navigate to announcement detail
                findNavController().navigate(
                    R.id.action_nav_announcements_to_announcementDetailFragment,
                    bundleOf("announcementId" to announcement.id)
                )
            }
        }
        
        binding.recyclerView.adapter = announcementAdapter
    }
    
    private fun observeData() {
        // Observe announcements
        viewModel.announcements.observe(viewLifecycleOwner) { announcements ->
            announcementAdapter.submitList(announcements)
            
            // Show empty view if list is empty
            binding.tvEmpty.visibility = if (announcements.isEmpty()) View.VISIBLE else View.GONE
        }
        
        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            
            // Stop refresh animation if it's running
            if (!isLoading && binding.swipeRefresh.isRefreshing) {
                binding.swipeRefresh.isRefreshing = false
            }
        }
        
        // Observe error messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                // Show error message
                // In a real app, you would show a Snackbar or Toast
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
