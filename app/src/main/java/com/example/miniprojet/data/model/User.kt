package com.example.miniprojet.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String?,
    val studentId: String?,
    val department: String?
)
