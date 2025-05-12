package com.example.trackforce.data.models

import java.util.Date

data class Task(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val assignedTo: String = "",
    val assignedBy: String = "",
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val dueDate: Date? = null,
    val priority: String = PRIORITY_MEDIUM,
    val status: String = STATUS_PENDING,
    val locationLatitude: Double? = null,
    val locationLongitude: Double? = null,
    val locationAddress: String? = null,
    val locationRadius: Float? = null
) {
    companion object {
        const val STATUS_PENDING = "PENDING"
        const val STATUS_IN_PROGRESS = "IN_PROGRESS"
        const val STATUS_COMPLETED = "COMPLETED"
        
        const val PRIORITY_LOW = "LOW"
        const val PRIORITY_MEDIUM = "MEDIUM"
        const val PRIORITY_HIGH = "HIGH"
    }
} 