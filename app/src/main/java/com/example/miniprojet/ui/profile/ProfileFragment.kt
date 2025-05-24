package com.example.miniprojet.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.miniprojet.R
import com.example.miniprojet.auth.GoogleAuthManager
import com.example.miniprojet.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: ProfileViewModel
    private lateinit var googleAuthManager: GoogleAuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize auth manager
        googleAuthManager = GoogleAuthManager(requireContext())
        
        // Initialize view model
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        
        // Set up update button
        binding.btnUpdate.setOnClickListener {
            updateProfile()
        }
        
        // Observe data
        observeData()
        
        // Load user profile
        loadUserProfile()
    }
    
    private fun loadUserProfile() {
        val account = googleAuthManager.getLastSignedInAccount()
        if (account != null) {
            viewModel.loadUserProfile(account.id ?: "")
        }
    }
    
    private fun updateProfile() {
        val name = binding.etName.text.toString().trim()
        val studentId = binding.etStudentId.text.toString().trim()
        val department = binding.etDepartment.text.toString().trim()
        
        if (name.isEmpty()) {
            binding.tilName.error = "Name is required"
            return
        }
        
        viewModel.updateUserProfile(name, studentId, department)
    }
    
    private fun observeData() {
        // Observe user profile
        viewModel.user.observe(viewLifecycleOwner) { user ->
            // Update UI with user profile
            binding.etName.setText(user.name)
            binding.etEmail.setText(user.email)
            binding.etStudentId.setText(user.studentId ?: "")
            binding.etDepartment.setText(user.department ?: "")
            
            // Load profile image
            user.photoUrl?.let {
                Glide.with(this)
                    .load(it)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(binding.ivProfile)
            }
        }
        
        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnUpdate.isEnabled = !isLoading
        }
        
        // Observe update status
        viewModel.updateStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is ProfileViewModel.UpdateStatus.Success -> {
                    Toast.makeText(requireContext(), R.string.profile_update_success, Toast.LENGTH_SHORT).show()
                }
                is ProfileViewModel.UpdateStatus.Error -> {
                    Toast.makeText(requireContext(), "${getString(R.string.profile_update_error)}: ${status.message}", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Loading or idle
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
