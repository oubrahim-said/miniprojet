package com.example.miniprojet.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miniprojet.auth.GoogleAuthManager
import com.example.miniprojet.databinding.FragmentDashboardBinding
import com.example.miniprojet.ui.announcements.AnnouncementAdapter
import com.example.miniprojet.ui.courses.CourseAdapter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: DashboardViewModel
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var announcementAdapter: AnnouncementAdapter
    private lateinit var googleAuthManager: GoogleAuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize auth manager
        googleAuthManager = GoogleAuthManager(requireContext())
        
        // Initialize view model
        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        
        // Set up recycler views
        setupRecyclerViews()
        
        // Observe data
        observeData()
        
        // Load data
        loadData()
    }
    
    private fun setupRecyclerViews() {
        // Set up courses recycler view
        courseAdapter = CourseAdapter()
        binding.rvUpcomingCourses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
        }
        
        // Set up announcements recycler view
        announcementAdapter = AnnouncementAdapter()
        binding.rvRecentAnnouncements.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = announcementAdapter
        }
    }
    
    private fun observeData() {
        // Observe upcoming courses
        viewModel.upcomingCourses.observe(viewLifecycleOwner) { courses ->
            courseAdapter.submitList(courses)
        }
        
        // Observe recent announcements
        viewModel.recentAnnouncements.observe(viewLifecycleOwner) { announcements ->
            announcementAdapter.submitList(announcements)
        }
    }
    
    private fun loadData() {
        // Get current user
        val account = googleAuthManager.getLastSignedInAccount()
        if (account != null) {
            // Update welcome message
            binding.tvWelcome.text = "Welcome, ${account.givenName}!"
            
            // Load data from repositories
            viewModel.loadData(account.id ?: "")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
