package com.example.trackforce.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trackforce.data.local.dao.AttendanceDao
import com.example.trackforce.data.local.dao.TaskDao
import com.example.trackforce.data.local.dao.UserDao
import com.example.trackforce.data.local.entity.AttendanceEntity
import com.example.trackforce.data.local.entity.TaskEntity
import com.example.trackforce.data.local.entity.UserEntity
import com.example.trackforce.data.local.util.Converters

@Database(
    entities = [
        UserEntity::class,
        AttendanceEntity::class,
        TaskEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TrackForceDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun taskDao(): TaskDao
} 