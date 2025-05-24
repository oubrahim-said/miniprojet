package com.example.miniprojet.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

enum class AnnouncementType {
    URGENT,
    EVENT,
    MISC
}

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val author: String,
    val date: Date,
    val type: AnnouncementType,
    val courseId: String? = null
)
