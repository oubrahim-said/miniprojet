package com.example.miniprojet.data.local

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import com.example.miniprojet.SmartStudentHubApp
import com.example.miniprojet.data.model.Document
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileNotFoundException

class DocumentContentProvider : ContentProvider() {
    
    companion object {
        private const val AUTHORITY = "com.example.miniprojet.documents"
        private const val DOCUMENTS = 1
        private const val DOCUMENT_ID = 2
        
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "documents", DOCUMENTS)
            addURI(AUTHORITY, "documents/*", DOCUMENT_ID)
        }
        
        fun getDocumentUri(documentId: String): Uri {
            return Uri.parse("content://$AUTHORITY/documents/$documentId")
        }
    }
    
    override fun onCreate(): Boolean {
        return true
    }
    
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val match = uriMatcher.match(uri)
        
        return when (match) {
            DOCUMENT_ID -> {
                val documentId = uri.lastPathSegment ?: return null
                val document = runBlocking {
                    SmartStudentHubApp.database.documentDao().getDocumentByIdSync(documentId)
                } ?: return null
                
                val cursor = MatrixCursor(
                    projection ?: arrayOf(
                        OpenableColumns.DISPLAY_NAME,
                        OpenableColumns.SIZE
                    )
                )
                
                cursor.addRow(
                    arrayOf(
                        document.title,
                        document.size
                    )
                )
                
                cursor
            }
            else -> null
        }
    }
    
    override fun getType(uri: Uri): String? {
        val match = uriMatcher.match(uri)
        
        return when (match) {
            DOCUMENT_ID -> {
                val documentId = uri.lastPathSegment ?: return null
                val document = runBlocking {
                    SmartStudentHubApp.database.documentDao().getDocumentByIdSync(documentId)
                } ?: return null
                
                document.mimeType
            }
            else -> null
        }
    }
    
    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val match = uriMatcher.match(uri)
        
        return when (match) {
            DOCUMENT_ID -> {
                val documentId = uri.lastPathSegment ?: throw FileNotFoundException("Document ID not found")
                val document = runBlocking {
                    SmartStudentHubApp.database.documentDao().getDocumentByIdSync(documentId)
                } ?: throw FileNotFoundException("Document not found")
                
                val file = File(document.filePath)
                if (!file.exists()) {
                    throw FileNotFoundException("File not found: ${file.absolutePath}")
                }
                
                ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            }
            else -> throw FileNotFoundException("Unknown URI: $uri")
        }
    }
    
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // Not implemented - documents are inserted through the app's UI
        return null
    }
    
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        // Not implemented - documents are deleted through the app's UI
        return 0
    }
    
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        // Not implemented - documents are updated through the app's UI
        return 0
    }
}
