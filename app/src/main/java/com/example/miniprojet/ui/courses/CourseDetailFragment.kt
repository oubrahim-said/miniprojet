package com.example.miniprojet.ui.courses

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miniprojet.R
import com.example.miniprojet.databinding.FragmentCourseDetailBinding
import com.example.miniprojet.ui.courses.exam.ExamAdapter
import java.text.SimpleDateFormat
import java.util.Locale

class CourseDetailFragment : Fragment() {

    private var _binding: FragmentCourseDetailBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: CourseDetailViewModel
    private lateinit var examAdapter: ExamAdapter
    private var courseId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Get course ID from arguments
        courseId = arguments?.getString("courseId")
        
        if (courseId == null) {
            // Handle error - no course ID provided
            return
        }
        
        // Initialize view model
        viewModel = ViewModelProvider(this)[CourseDetailViewModel::class.java]
        
        // Set up exam recycler view
        setupExamRecyclerView()
        
        // Set up favorite button
        setupFavoriteButton()
        
        // Set up resources button
        setupResourcesButton()
        
        // Observe data
        observeData()
        
        // Load data
        viewModel.loadCourseDetails(courseId!!)
        viewModel.loadExamSchedule(courseId!!)
    }
    
    private fun setupExamRecyclerView() {
        examAdapter = ExamAdapter()
        binding.rvExams.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = examAdapter
        }
    }
    
    private fun setupFavoriteButton() {
        binding.btnFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }
    
    private fun setupResourcesButton() {
        binding.btnResources.setOnClickListener {
            viewModel.course.value?.resourcesUrl?.let { url ->
                if (url.isNotEmpty()) {
                    // Open URL in browser
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }
            }
        }
    }
    
    private fun observeData() {
        // Observe course details
        viewModel.course.observe(viewLifecycleOwner) { course ->
            // Update UI with course details
            binding.tvCourseTitle.text = course.title
            binding.tvProfessor.text = course.professorName
            binding.tvSchedule.text = course.schedule
            binding.tvDescription.text = course.description
            
            // Update favorite button
            updateFavoriteButton(course.isFavorite)
            
            // Show/hide resources button
            binding.btnResources.visibility = if (course.resourcesUrl.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
        
        // Observe exam schedule
        viewModel.exams.observe(viewLifecycleOwner) { exams ->
            examAdapter.submitList(exams)
            
            // Show/hide no exams message
            binding.tvNoExams.visibility = if (exams.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        
        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
    
    private fun updateFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) {
            binding.btnFavorite.text = getString(R.string.remove_from_favorites)
            binding.btnFavorite.setIconResource(R.drawable.ic_favorite)
        } else {
            binding.btnFavorite.text = getString(R.string.add_to_favorites)
            binding.btnFavorite.setIconResource(R.drawable.ic_favorite_border)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
