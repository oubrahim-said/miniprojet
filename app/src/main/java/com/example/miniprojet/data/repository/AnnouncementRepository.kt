package com.example.miniprojet.data.repository

import androidx.lifecycle.LiveData
import com.example.miniprojet.SmartStudentHubApp
import com.example.miniprojet.data.model.Announcement
import com.example.miniprojet.data.model.AnnouncementType
import com.example.miniprojet.data.remote.ClassroomApiService
import com.example.miniprojet.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AnnouncementRepository {
    private val announcementDao = SmartStudentHubApp.database.announcementDao()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://classroom.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    private val classroomApiService = retrofit.create(ClassroomApiService::class.java)
    
    fun getAllAnnouncements(): LiveData<List<Announcement>> {
        return announcementDao.getAllAnnouncements()
    }
    
    fun getAnnouncementsByType(type: AnnouncementType): LiveData<List<Announcement>> {
        return announcementDao.getAnnouncementsByType(type)
    }
    
    fun getAnnouncementsByCourse(courseId: String): LiveData<List<Announcement>> {
        return announcementDao.getAnnouncementsByCourse(courseId)
    }
    
    fun getAnnouncementById(announcementId: String): LiveData<Announcement> {
        return announcementDao.getAnnouncementById(announcementId)
    }
    
    suspend fun refreshAnnouncements(authToken: String, courseId: String): NetworkResult<List<Announcement>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = classroomApiService.getAnnouncements("Bearer $authToken", courseId)
                
                if (response.isSuccessful) {
                    val announcementResponses = response.body()?.announcements ?: emptyList()
                    val announcements = announcementResponses.map { announcementResponse ->
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                        val date = try {
                            dateFormat.parse(announcementResponse.creationTime) ?: Date()
                        } catch (e: Exception) {
                            Date()
                        }
                        
                        // Determine announcement type (this is a simplification)
                        val type = when {
                            announcementResponse.text.contains("urgent", ignoreCase = true) -> AnnouncementType.URGENT
                            announcementResponse.text.contains("event", ignoreCase = true) -> AnnouncementType.EVENT
                            else -> AnnouncementType.MISC
                        }
                        
                        Announcement(
                            id = announcementResponse.id,
                            title = announcementResponse.text.split("\n").firstOrNull() ?: "Announcement",
                            content = announcementResponse.text,
                            author = "Instructor", // Not available in API response
                            date = date,
                            type = type,
                            courseId = courseId
                        )
                    }
                    
                    // Save to database
                    announcementDao.insertAnnouncements(announcements)
                    
                    NetworkResult.Success(announcements)
                } else {
                    NetworkResult.Error("Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Network error: ${e.message}")
            }
        }
    }
}
