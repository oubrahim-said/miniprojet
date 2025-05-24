package com.example.miniprojet.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.miniprojet.data.model.Document

@Dao
interface DocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: Document)
    
    @Query("SELECT * FROM documents ORDER BY uploadDate DESC")
    fun getAllDocuments(): LiveData<List<Document>>
    
    @Query("SELECT * FROM documents WHERE authorId = :authorId ORDER BY uploadDate DESC")
    fun getDocumentsByAuthor(authorId: String): LiveData<List<Document>>
    
    @Query("SELECT * FROM documents WHERE id = :documentId")
    fun getDocumentById(documentId: String): LiveData<Document>
    
    @Query("SELECT * FROM documents WHERE id = :documentId")
    suspend fun getDocumentByIdSync(documentId: String): Document?
    
    @Query("DELETE FROM documents WHERE id = :documentId")
    suspend fun deleteDocument(documentId: String)
}
