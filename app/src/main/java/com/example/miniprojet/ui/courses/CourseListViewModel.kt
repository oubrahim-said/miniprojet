package com.example.miniprojet.ui.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniprojet.auth.GoogleAuthManager
import com.example.miniprojet.data.model.Course
import com.example.miniprojet.data.repository.CourseRepository
import com.example.miniprojet.utils.NetworkResult
import kotlinx.coroutines.launch

class CourseListViewModel : ViewModel() {
    
    private val courseRepository = CourseRepository()
    
    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>> = _courses
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    private var currentType: String = CourseListFragment.TYPE_ALL
    
    fun loadCourses(type: String) {
        currentType = type
        _isLoading.value = true
        
        viewModelScope.launch {
            when (type) {
                CourseListFragment.TYPE_CURRENT -> {
                    courseRepository.getCurrentCourses().observeForever { courses ->
                        _courses.value = courses
                        _isLoading.value = false
                    }
                }
                CourseListFragment.TYPE_COMPLETED -> {
                    courseRepository.getCompletedCourses().observeForever { courses ->
                        _courses.value = courses
                        _isLoading.value = false
                    }
                }
                else -> { // TYPE_ALL
                    courseRepository.getAllCourses().observeForever { courses ->
                        _courses.value = courses
                        _isLoading.value = false
                    }
                }
            }
        }
    }
    
    fun refreshCourses() {
        _isLoading.value = true
        _errorMessage.value = ""
        
        viewModelScope.launch {
            // In a real app, you would get the auth token from the GoogleAuthManager
            val authToken = "dummy_token" // This is just a placeholder
            
            when (val result = courseRepository.refreshCourses(authToken)) {
                is NetworkResult.Success -> {
                    // Reload courses from database
                    loadCourses(currentType)
                }
                is NetworkResult.Error -> {
                    _errorMessage.value = result.message
                    _isLoading.value = false
                }
                is NetworkResult.Loading -> {
                    // Already set loading to true above
                }
            }
        }
    }
    
    fun updateFavoriteStatus(courseId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            courseRepository.updateFavoriteStatus(courseId, isFavorite)
        }
    }
}
