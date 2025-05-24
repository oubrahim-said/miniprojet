package com.example.miniprojet.data.repository

import androidx.lifecycle.LiveData
import com.example.miniprojet.SmartStudentHubApp
import com.example.miniprojet.data.model.User

class UserRepository {
    private val userDao = SmartStudentHubApp.database.userDao()
    
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
    
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
    
    fun getUserById(userId: String): LiveData<User> {
        return userDao.getUserById(userId)
    }
    
    suspend fun getUserByIdSync(userId: String): User? {
        return userDao.getUserByIdSync(userId)
    }
    
    suspend fun deleteUser(userId: String) {
        userDao.deleteUser(userId)
    }
}
