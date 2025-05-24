package com.example.miniprojet.ui.announcements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniprojet.data.model.Announcement
import com.example.miniprojet.data.model.AnnouncementType
import com.example.miniprojet.data.repository.AnnouncementRepository
import com.example.miniprojet.utils.NetworkResult
import kotlinx.coroutines.launch

class AnnouncementListViewModel : ViewModel() {
    
    private val announcementRepository = AnnouncementRepository()
    
    private val _announcements = MutableLiveData<List<Announcement>>()
    val announcements: LiveData<List<Announcement>> = _announcements
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    private var currentType: AnnouncementType = AnnouncementType.MISC
    
    fun loadAnnouncements(type: AnnouncementType) {
        currentType = type
        _isLoading.value = true
        
        viewModelScope.launch {
            announcementRepository.getAnnouncementsByType(type).observeForever { announcements ->
                _announcements.value = announcements
                _isLoading.value = false
            }
        }
    }
    
    fun refreshAnnouncements() {
        _isLoading.value = true
        _errorMessage.value = ""
        
        viewModelScope.launch {
            // In a real app, you would get the auth token from the GoogleAuthManager
            val authToken = "dummy_token" // This is just a placeholder
            val courseId = "dummy_course_id" // This is just a placeholder
            
            when (val result = announcementRepository.refreshAnnouncements(authToken, courseId)) {
                is NetworkResult.Success -> {
                    // Reload announcements from database
                    loadAnnouncements(currentType)
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
}
