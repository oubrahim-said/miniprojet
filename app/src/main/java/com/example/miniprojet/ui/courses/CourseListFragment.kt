package com.example.miniprojet.ui.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.miniprojet.R
import com.example.miniprojet.databinding.FragmentCourseListBinding

class CourseListFragment : Fragment() {

    companion object {
        const val TYPE_CURRENT = "current"
        const val TYPE_COMPLETED = "completed"
        const val TYPE_ALL = "all"
        
        private const val ARG_TYPE = "type"
        
        fun newInstance(type: String): CourseListFragment {
            return CourseListFragment().apply {
                arguments = bundleOf(ARG_TYPE to type)
            }
        }
    }
    
    private var _binding: FragmentCourseListBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: CourseListViewModel
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var courseType: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Get course type from arguments
        courseType = arguments?.getString(ARG_TYPE) ?: TYPE_ALL
        
        // Initialize view model
        viewModel = ViewModelProvider(this)[CourseListViewModel::class.java]
        
        // Set up recycler view
        setupRecyclerView()
        
        // Set up swipe refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshCourses()
        }
        
        // Observe data
        observeData()
        
        // Load data
        viewModel.loadCourses(courseType)
    }
    
    private fun setupRecyclerView() {
        courseAdapter = CourseAdapter().apply {
            setOnItemClickListener { course ->
                // Navigate to course detail
                findNavController().navigate(
                    R.id.action_nav_courses_to_courseDetailFragment,
                    bundleOf("courseId" to course.id)
                )
            }
            
            setOnFavoriteClickListener { course, isFavorite ->
                // Update favorite status
                viewModel.updateFavoriteStatus(course.id, isFavorite)
            }
        }
        
        binding.recyclerView.adapter = courseAdapter
    }
    
    private fun observeData() {
        // Observe courses
        viewModel.courses.observe(viewLifecycleOwner) { courses ->
            courseAdapter.submitList(courses)
            
            // Show empty view if list is empty
            binding.tvEmpty.visibility = if (courses.isEmpty()) View.VISIBLE else View.GONE
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
