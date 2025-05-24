package com.example.miniprojet.ui.announcements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.miniprojet.data.model.Announcement
import com.example.miniprojet.data.repository.AnnouncementRepository

class AnnouncementDetailViewModel : ViewModel() {
    
    private val announcementRepository = AnnouncementRepository()
    
    private val _announcement = MutableLiveData<Announcement>()
    val announcement: LiveData<Announcement> = _announcement
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    fun loadAnnouncementDetails(announcementId: String) {
        _isLoading.value = true
        
        announcementRepository.getAnnouncementById(announcementId).observeForever { announcement ->
            _announcement.value = announcement
            _isLoading.value = false
        }
    }
}
