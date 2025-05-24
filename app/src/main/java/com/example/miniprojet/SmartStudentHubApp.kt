package com.example.miniprojet

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.miniprojet.data.local.StudentHubDatabase

class SmartStudentHubApp : Application() {

    companion object {
        lateinit var instance: SmartStudentHubApp
            private set

        // Database instance
        lateinit var database: StudentHubDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize Room database
        database = Room.databaseBuilder(
            applicationContext,
            StudentHubDatabase::class.java,
            "smart_student_hub_db"
        ).build()
    }
}
