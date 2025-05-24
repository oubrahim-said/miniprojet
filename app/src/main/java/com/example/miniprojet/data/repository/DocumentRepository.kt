package com.example.miniprojet.data.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.miniprojet.SmartStudentHubApp
import com.example.miniprojet.data.local.DocumentContentProvider
import com.example.miniprojet.data.model.Document
import com.example.miniprojet.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date
import java.util.UUID

class DocumentRepository {
    private val documentDao = SmartStudentHubApp.database.documentDao()
    private val context = SmartStudentHubApp.instance.applicationContext
    
    fun getAllDocuments(): LiveData<List<Document>> {
        return documentDao.getAllDocuments()
    }
    
    fun getDocumentsByAuthor(authorId: String): LiveData<List<Document>> {
        return documentDao.getDocumentsByAuthor(authorId)
    }
    
    fun getDocumentById(documentId: String): LiveData<Document> {
        return documentDao.getDocumentById(documentId)
    }
    
    suspend fun getDocumentByIdSync(documentId: String): Document? {
        return documentDao.getDocumentByIdSync(documentId)
    }
    
    suspend fun uploadDocument(
        uri: Uri,
        title: String,
        description: String,
        currentUser: User
    ): Result<Document> = withContext(Dispatchers.IO) {
        try {
            // Get file details
            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"
            
            // Create documents directory if it doesn't exist
            val documentsDir = File(context.filesDir, "documents")
            if (!documentsDir.exists()) {
                documentsDir.mkdirs()
            }
            
            // Generate unique filename
            val documentId = UUID.randomUUID().toString()
            val fileExtension = mimeType.split("/").lastOrNull()?.let { ".$it" } ?: ""
            val destinationFile = File(documentsDir, "$documentId$fileExtension")
            
            // Copy file
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: throw IOException("Failed to open input stream")
            
            // Create document object
            val document = Document(
                id = documentId,
                title = title,
                description = description,
                filePath = destinationFile.absolutePath,
                mimeType = mimeType,
                size = destinationFile.length(),
                uploadDate = Date(),
                authorId = currentUser.id,
                authorName = currentUser.name
            )
            
            // Save to database
            documentDao.insertDocument(document)
            
            Result.success(document)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteDocument(documentId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val document = documentDao.getDocumentByIdSync(documentId)
            
            if (document != null) {
                // Delete file
                val file = File(document.filePath)
                if (file.exists()) {
                    file.delete()
                }
                
                // Delete from database
                documentDao.deleteDocument(documentId)
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getDocumentUri(documentId: String): Uri {
        return DocumentContentProvider.getDocumentUri(documentId)
    }
}
