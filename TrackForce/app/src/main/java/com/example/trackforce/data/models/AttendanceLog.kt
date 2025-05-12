package com.example.trackforce.data.models

import java.util.Date

data class AttendanceLog(
    val id: String = "",
    val userId: String = "",
    val timestamp: Date = Date(),
    val type: String = "", // CLOCK_IN or CLOCK_OUT
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val locationAccuracy: Float = 0f,
    val deviceManufacturer: String = "",
    val deviceModel: String = "",
    val deviceOsVersion: String = "",
    val appVersion: String = "",
    val batteryLevel: Int = 0,
    val networkConnected: Boolean = false,
    val networkType: String = "",
    val networkSignalStrength: Int = 0,
    val status: String = STATUS_PENDING,
    val errorMessage: String? = null,
    val verifiedBy: String? = null,
    val verifiedAt: Date? = null
) {
    companion object {
        const val STATUS_PENDING = "PENDING"
        const val STATUS_VERIFIED = "VERIFIED"
        const val STATUS_REJECTED = "REJECTED"
        
        const val TYPE_CLOCK_IN = "CLOCK_IN"
        const val TYPE_CLOCK_OUT = "CLOCK_OUT"
    }
} 