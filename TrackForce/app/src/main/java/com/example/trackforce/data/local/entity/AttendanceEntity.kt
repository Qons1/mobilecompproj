package com.example.trackforce.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "attendance_logs",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["uid"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("userId"),
        Index("timestamp")
    ]
)
data class AttendanceEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val timestamp: Date?,
    val type: String,
    val latitude: Double,
    val longitude: Double,
    val locationAccuracy: Float,
    val deviceManufacturer: String,
    val deviceModel: String,
    val deviceOsVersion: String,
    val appVersion: String,
    val batteryLevel: Int,
    val networkConnected: Boolean,
    val networkType: String,
    val networkSignalStrength: Int,
    val status: String,
    val errorMessage: String?,
    val verifiedBy: String?,
    val verifiedAt: Date?,
    val syncStatus: String = "PENDING" // LOCAL, SYNCED, PENDING, ERROR
) 