package com.example.miniprojet.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "documents")
data class Document(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val filePath: String,
    val mimeType: String,
    val size: Long,
    val uploadDate: Date,
    val authorId: String,
    val authorName: String
)
