package com.example.trackforce.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["uid"],
            childColumns = ["assignedTo"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["uid"],
            childColumns = ["assignedBy"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("assignedTo"),
        Index("assignedBy"),
        Index("dueDate")
    ]
)
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val assignedTo: String,
    val assignedBy: String,
    val createdAt: Date?,
    val updatedAt: Date?,
    val dueDate: Date?,
    val priority: String,
    val status: String,
    val locationLatitude: Double?,
    val locationLongitude: Double?,
    val locationAddress: String?,
    val locationRadius: Float?,
    val syncStatus: String = "PENDING" // LOCAL, SYNCED, PENDING, ERROR
) 