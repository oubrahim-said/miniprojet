package com.example.miniprojet.data.repository

import androidx.lifecycle.LiveData
import com.example.miniprojet.SmartStudentHubApp
import com.example.miniprojet.data.model.Course
import com.example.miniprojet.data.remote.ClassroomApiService
import com.example.miniprojet.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CourseRepository {
    private val courseDao = SmartStudentHubApp.database.courseDao()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://classroom.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    private val classroomApiService = retrofit.create(ClassroomApiService::class.java)
    
    fun getAllCourses(): LiveData<List<Course>> {
        return courseDao.getAllCourses()
    }
    
    fun getCurrentCourses(): LiveData<List<Course>> {
        return courseDao.getCurrentCourses()
    }
    
    fun getCompletedCourses(): LiveData<List<Course>> {
        return courseDao.getCompletedCourses()
    }
    
    fun getFavoriteCourses(): LiveData<List<Course>> {
        return courseDao.getFavoriteCourses()
    }
    
    fun getCourseById(courseId: String): LiveData<Course> {
        return courseDao.getCourseById(courseId)
    }
    
    suspend fun updateFavoriteStatus(courseId: String, isFavorite: Boolean) {
        courseDao.updateFavoriteStatus(courseId, isFavorite)
    }
    
    suspend fun refreshCourses(authToken: String): NetworkResult<List<Course>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = classroomApiService.getCourses("Bearer $authToken")
                
                if (response.isSuccessful) {
                    val courseResponses = response.body()?.courses ?: emptyList()
                    val courses = courseResponses.map { courseResponse ->
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                        val startDate = try {
                            dateFormat.parse(courseResponse.creationTime) ?: Date()
                        } catch (e: Exception) {
                            Date()
                        }
                        
                        Course(
                            id = courseResponse.id,
                            title = courseResponse.name,
                            description = courseResponse.description ?: "",
                            professorName = "Professor", // Not available in API response
                            startDate = startDate,
                            endDate = null, // Not available in API response
                            schedule = "TBD", // Not available in API response
                            isCompleted = courseResponse.courseState == "ARCHIVED",
                            isFavorite = false,
                            resourcesUrl = null
                        )
                    }
                    
                    // Save to database
                    courseDao.insertCourses(courses)
                    
                    NetworkResult.Success(courses)
                } else {
                    NetworkResult.Error("Error: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Network error: ${e.message}")
            }
        }
    }
}
