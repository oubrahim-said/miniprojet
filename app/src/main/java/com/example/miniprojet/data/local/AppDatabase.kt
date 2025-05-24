package com.example.miniprojet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.miniprojet.data.local.dao.AnnouncementDao
import com.example.miniprojet.data.local.dao.CourseDao
import com.example.miniprojet.data.local.dao.DocumentDao
import com.example.miniprojet.data.local.dao.UserDao
import com.example.miniprojet.data.model.Announcement
import com.example.miniprojet.data.model.Course
import com.example.miniprojet.data.model.Document
import com.example.miniprojet.data.model.User
import com.example.miniprojet.utils.DateConverter

@Database(
    entities = [
        User::class,
        Course::class,
        Announcement::class,
        Document::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun courseDao(): CourseDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun documentDao(): DocumentDao
}
