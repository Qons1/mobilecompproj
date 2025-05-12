package com.example.trackforce.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val uid: String,
    val email: String,
    val role: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String?,
    val phoneNumber: String?,
    val department: String,
    val isActive: Boolean,
    val createdAt: Date?,
    val lastUpdatedAt: Date?
) 