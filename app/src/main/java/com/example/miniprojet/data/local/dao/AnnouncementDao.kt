package com.example.miniprojet.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.miniprojet.data.model.Announcement
import com.example.miniprojet.data.model.AnnouncementType

@Dao
interface AnnouncementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: Announcement)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncements(announcements: List<Announcement>)
    
    @Query("SELECT * FROM announcements ORDER BY date DESC")
    fun getAllAnnouncements(): LiveData<List<Announcement>>
    
    @Query("SELECT * FROM announcements WHERE type = :type ORDER BY date DESC")
    fun getAnnouncementsByType(type: AnnouncementType): LiveData<List<Announcement>>
    
    @Query("SELECT * FROM announcements WHERE courseId = :courseId ORDER BY date DESC")
    fun getAnnouncementsByCourse(courseId: String): LiveData<List<Announcement>>
    
    @Query("SELECT * FROM announcements WHERE id = :announcementId")
    fun getAnnouncementById(announcementId: String): LiveData<Announcement>
    
    @Query("DELETE FROM announcements")
    suspend fun deleteAllAnnouncements()
}
