package com.example.miniprojet.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val professorName: String,
    val startDate: Date,
    val endDate: Date?,
    val schedule: String,
    val isCompleted: Boolean = false,
    val isFavorite: Boolean = false,
    val resourcesUrl: String? = null
)
