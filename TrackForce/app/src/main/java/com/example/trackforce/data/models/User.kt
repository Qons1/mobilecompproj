package com.example.trackforce.data.models

import androidx.annotation.NonNull
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User(
    @NonNull
    val uid: String = "",
    
    @NonNull
    val email: String = "",
    
    @NonNull
    val role: String = "", // "MANAGER" or "EMPLOYEE"
    
    val firstName: String = "",
    val lastName: String = "",
    val profilePictureUrl: String? = null,
    val phoneNumber: String? = null,
    val department: String = "",
    val isActive: Boolean = true,
    
    @ServerTimestamp
    val createdAt: Date? = null,
    
    @ServerTimestamp
    val lastUpdatedAt: Date? = null
) {
    companion object {
        const val ROLE_MANAGER = "MANAGER"
        const val ROLE_EMPLOYEE = "EMPLOYEE"
    }

    fun getFullName(): String = "$firstName $lastName"
    
    fun isManager(): Boolean = role == ROLE_MANAGER
} 