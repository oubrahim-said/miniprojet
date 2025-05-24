package com.example.miniprojet.ui.documents

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miniprojet.R
import com.example.miniprojet.auth.GoogleAuthManager
import com.example.miniprojet.databinding.FragmentDocumentsBinding
import com.example.miniprojet.ui.documents.upload.UploadDocumentDialogFragment

class DocumentsFragment : Fragment() {

    private var _binding: FragmentDocumentsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: DocumentsViewModel
    private lateinit var documentAdapter: DocumentAdapter
    private lateinit var googleAuthManager: GoogleAuthManager
    
    private val pickDocument = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                // Show upload dialog
                showUploadDialog(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDocumentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize auth manager
        googleAuthManager = GoogleAuthManager(requireContext())
        
        // Initialize view model
        viewModel = ViewModelProvider(this)[DocumentsViewModel::class.java]
        
        // Set up recycler view
        setupRecyclerView()
        
        // Set up upload button
        binding.btnUpload.setOnClickListener {
            openDocumentPicker()
        }
        
        // Set up swipe refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadDocuments()
        }
        
        // Observe data
        observeData()
        
        // Load data
        viewModel.loadDocuments()
    }
    
    private fun setupRecyclerView() {
        documentAdapter = DocumentAdapter(
            onDownloadClick = { document ->
                viewModel.downloadDocument(document)
            },
            onShareClick = { document ->
                viewModel.shareDocument(document)
            },
            onDeleteClick = { document ->
                viewModel.deleteDocument(document.id)
            }
        )
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = documentAdapter
        }
    }
    
    private fun openDocumentPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "text/plain",
                "image/jpeg",
                "image/png"
            ))
        }
        pickDocument.launch(intent)
    }
    
    private fun showUploadDialog(uri: Uri) {
        val dialog = UploadDocumentDialogFragment.newInstance(uri.toString())
        dialog.setOnUploadListener { title, description ->
            // Get current user
            val account = googleAuthManager.getLastSignedInAccount()
            if (account != null) {
                viewModel.uploadDocument(uri, title, description, account.id ?: "")
            } else {
                Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show(childFragmentManager, "UploadDocumentDialog")
    }
    
    private fun observeData() {
        // Observe documents
        viewModel.documents.observe(viewLifecycleOwner) { documents ->
            documentAdapter.submitList(documents)
            
            // Show empty view if list is empty
            binding.tvEmpty.visibility = if (documents.isEmpty()) View.VISIBLE else View.GONE
        }
        
        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            
            // Stop refresh animation if it's running
            if (!isLoading && binding.swipeRefresh.isRefreshing) {
                binding.swipeRefresh.isRefreshing = false
            }
        }
        
        // Observe upload status
        viewModel.uploadStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is DocumentsViewModel.UploadStatus.Success -> {
                    Toast.makeText(requireContext(), R.string.document_upload_success, Toast.LENGTH_SHORT).show()
                }
                is DocumentsViewModel.UploadStatus.Error -> {
                    Toast.makeText(requireContext(), "${getString(R.string.document_upload_error)}: ${status.message}", Toast.LENGTH_SHORT).show()
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
