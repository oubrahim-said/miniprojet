package com.example.miniprojet.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniprojet.data.model.User
import com.example.miniprojet.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    
    private val userRepository = UserRepository()
    
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _updateStatus = MutableLiveData<UpdateStatus>()
    val updateStatus: LiveData<UpdateStatus> = _updateStatus
    
    private var userId: String? = null
    
    fun loadUserProfile(userId: String) {
        this.userId = userId
        _isLoading.value = true
        
        userRepository.getUserById(userId).observeForever { user ->
            _user.value = user
            _isLoading.value = false
        }
    }
    
    fun updateUserProfile(name: String, studentId: String, department: String) {
        val currentUser = _user.value ?: return
        val userId = this.userId ?: return
        
        _isLoading.value = true
        _updateStatus.value = UpdateStatus.Loading
        
        viewModelScope.launch {
            try {
                // Create updated user
                val updatedUser = currentUser.copy(
                    name = name,
                    studentId = studentId.ifEmpty { null },
                    department = department.ifEmpty { null }
                )
                
                // Update user in database
                userRepository.updateUser(updatedUser)
                
                _updateStatus.value = UpdateStatus.Success
                _isLoading.value = false
            } catch (e: Exception) {
                _updateStatus.value = UpdateStatus.Error(e.message ?: "Unknown error")
                _isLoading.value = false
            }
        }
    }
    
    sealed class UpdateStatus {
        object Idle : UpdateStatus()
        object Loading : UpdateStatus()
        object Success : UpdateStatus()
        data class Error(val message: String) : UpdateStatus()
    }
}
