package com.example.miniprojet.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.miniprojet.data.model.Course

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<Course>)
    
    @Update
    suspend fun updateCourse(course: Course)
    
    @Query("SELECT * FROM courses")
    fun getAllCourses(): LiveData<List<Course>>
    
    @Query("SELECT * FROM courses WHERE isCompleted = 0")
    fun getCurrentCourses(): LiveData<List<Course>>
    
    @Query("SELECT * FROM courses WHERE isCompleted = 1")
    fun getCompletedCourses(): LiveData<List<Course>>
    
    @Query("SELECT * FROM courses WHERE isFavorite = 1")
    fun getFavoriteCourses(): LiveData<List<Course>>
    
    @Query("SELECT * FROM courses WHERE id = :courseId")
    fun getCourseById(courseId: String): LiveData<Course>
    
    @Query("UPDATE courses SET isFavorite = :isFavorite WHERE id = :courseId")
    suspend fun updateFavoriteStatus(courseId: String, isFavorite: Boolean)
    
    @Query("DELETE FROM courses")
    suspend fun deleteAllCourses()
}
