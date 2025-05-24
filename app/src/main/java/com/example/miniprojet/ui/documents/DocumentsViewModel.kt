package com.example.miniprojet.ui.documents

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniprojet.SmartStudentHubApp
import com.example.miniprojet.data.model.Document
import com.example.miniprojet.data.model.User
import com.example.miniprojet.data.repository.DocumentRepository
import com.example.miniprojet.data.repository.UserRepository
import kotlinx.coroutines.launch

class DocumentsViewModel : ViewModel() {
    
    private val documentRepository = DocumentRepository()
    private val userRepository = UserRepository()
    
    private val _documents = MutableLiveData<List<Document>>()
    val documents: LiveData<List<Document>> = _documents
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _uploadStatus = MutableLiveData<UploadStatus>()
    val uploadStatus: LiveData<UploadStatus> = _uploadStatus
    
    fun loadDocuments() {
        _isLoading.value = true
        
        documentRepository.getAllDocuments().observeForever { documents ->
            _documents.value = documents
            _isLoading.value = false
        }
    }
    
    fun uploadDocument(uri: Uri, title: String, description: String, userId: String) {
        _isLoading.value = true
        _uploadStatus.value = UploadStatus.Loading
        
        viewModelScope.launch {
            try {
                // Get user from database
                val user = userRepository.getUserByIdSync(userId)
                
                if (user != null) {
                    // Upload document
                    val result = documentRepository.uploadDocument(uri, title, description, user)
                    
                    if (result.isSuccess) {
                        _uploadStatus.value = UploadStatus.Success
                        // Reload documents
                        loadDocuments()
                    } else {
                        _uploadStatus.value = UploadStatus.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                        _isLoading.value = false
                    }
                } else {
                    _uploadStatus.value = UploadStatus.Error("User not found")
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _uploadStatus.value = UploadStatus.Error(e.message ?: "Unknown error")
                _isLoading.value = false
            }
        }
    }
    
    fun downloadDocument(document: Document) {
        // In a real app, this would download the document to the device
        // For this example, we'll just create an intent to view the document
        val uri = documentRepository.getDocumentUri(document.id)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, document.mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        
        // The intent will be handled by the fragment
        SmartStudentHubApp.instance.startActivity(intent)
    }
    
    fun shareDocument(document: Document) {
        val uri = documentRepository.getDocumentUri(document.id)
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, uri)
            type = document.mimeType
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        
        val shareIntent = Intent.createChooser(intent, "Share Document")
        SmartStudentHubApp.instance.startActivity(shareIntent)
    }
    
    fun deleteDocument(documentId: String) {
        _isLoading.value = true
        
        viewModelScope.launch {
            val result = documentRepository.deleteDocument(documentId)
            
            if (result.isSuccess) {
                // Reload documents
                loadDocuments()
            } else {
                // Handle error
                _isLoading.value = false
            }
        }
    }
    
    sealed class UploadStatus {
        object Idle : UploadStatus()
        object Loading : UploadStatus()
        object Success : UploadStatus()
        data class Error(val message: String) : UploadStatus()
    }
}
