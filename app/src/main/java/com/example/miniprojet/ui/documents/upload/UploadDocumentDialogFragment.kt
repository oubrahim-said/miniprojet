package com.example.miniprojet.ui.documents.upload

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.miniprojet.R
import com.example.miniprojet.databinding.DialogUploadDocumentBinding
import java.io.File

class UploadDocumentDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_URI = "uri"
        
        fun newInstance(uriString: String): UploadDocumentDialogFragment {
            return UploadDocumentDialogFragment().apply {
                arguments = bundleOf(ARG_URI to uriString)
            }
        }
    }
    
    private var _binding: DialogUploadDocumentBinding? = null
    private val binding get() = _binding!!
    
    private var onUploadListener: ((String, String) -> Unit)? = null
    
    fun setOnUploadListener(listener: (String, String) -> Unit) {
        onUploadListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogUploadDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Get URI from arguments
        val uriString = arguments?.getString(ARG_URI)
        if (uriString == null) {
            dismiss()
            return
        }
        
        val uri = Uri.parse(uriString)
        
        // Show selected file name
        val fileName = getFileName(uri)
        binding.tvSelectedFile.text = "Selected file: $fileName"
        
        // Pre-fill title with file name (without extension)
        val titleWithoutExtension = fileName.substringBeforeLast(".")
        binding.etDocumentTitle.setText(titleWithoutExtension)
        
        // Set up buttons
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        
        binding.btnUpload.setOnClickListener {
            val title = binding.etDocumentTitle.text.toString().trim()
            val description = binding.etDocumentDescription.text.toString().trim()
            
            if (title.isEmpty()) {
                binding.tilDocumentTitle.error = "Title is required"
                return@setOnClickListener
            }
            
            onUploadListener?.invoke(title, description)
            dismiss()
        }
    }
    
    private fun getFileName(uri: Uri): String {
        val context = requireContext()
        val contentResolver = context.contentResolver
        
        // Try to get the display name from the content resolver
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex("_display_name")
                if (displayNameIndex != -1) {
                    return cursor.getString(displayNameIndex)
                }
            }
        }
        
        // If that fails, try to get the last path segment
        uri.lastPathSegment?.let {
            return File(it).name
        }
        
        // If all else fails, return a default name
        return "document"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
