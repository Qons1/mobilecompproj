package com.example.trackforce.models

data class AttendanceLog(
    val userId: String = "",
    val timestamp: Long = 0L,
    val type: String = "", // "in" or "out"
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

