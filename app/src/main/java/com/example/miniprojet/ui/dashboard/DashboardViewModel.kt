package com.example.miniprojet.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniprojet.data.model.Announcement
import com.example.miniprojet.data.model.Course
import com.example.miniprojet.data.repository.AnnouncementRepository
import com.example.miniprojet.data.repository.CourseRepository
import kotlinx.coroutines.launch
import java.util.Date

class DashboardViewModel : ViewModel() {
    
    private val courseRepository = CourseRepository()
    private val announcementRepository = AnnouncementRepository()
    
    private val _upcomingCourses = MutableLiveData<List<Course>>()
    val upcomingCourses: LiveData<List<Course>> = _upcomingCourses
    
    private val _recentAnnouncements = MutableLiveData<List<Announcement>>()
    val recentAnnouncements: LiveData<List<Announcement>> = _recentAnnouncements
    
    fun loadData(userId: String) {
        viewModelScope.launch {
            // Load upcoming courses (non-completed courses)
            val courses = courseRepository.getCurrentCourses()
            courses.observeForever { courseList ->
                _upcomingCourses.value = courseList.take(5) // Show only the first 5 courses
            }
            
            // Load recent announcements
            val announcements = announcementRepository.getAllAnnouncements()
            announcements.observeForever { announcementList ->
                _recentAnnouncements.value = announcementList
                    .sortedByDescending { it.date }
                    .take(5) // Show only the first 5 announcements
            }
        }
    }
}
