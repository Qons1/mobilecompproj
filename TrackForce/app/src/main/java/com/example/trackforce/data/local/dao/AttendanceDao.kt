package com.example.trackforce.data.local.dao

import androidx.room.*
import com.example.trackforce.data.local.entity.AttendanceEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: AttendanceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(logs: List<AttendanceEntity>)

    @Update
    suspend fun update(log: AttendanceEntity)

    @Delete
    suspend fun delete(log: AttendanceEntity)

    @Query("SELECT * FROM attendance_logs WHERE id = :id")
    suspend fun get(id: String): AttendanceEntity?

    @Query("SELECT * FROM attendance_logs WHERE id = :id")
    fun getStream(id: String): Flow<AttendanceEntity?>

    @Query("SELECT * FROM attendance_logs ORDER BY timestamp DESC")
    fun getAll(): Flow<List<AttendanceEntity>>

    @Query("""
        SELECT * FROM attendance_logs 
        WHERE userId = :userId 
        AND (:startDate IS NULL OR timestamp >= :startDate)
        AND (:endDate IS NULL OR timestamp <= :endDate)
        ORDER BY timestamp DESC
    """)
    fun getUserLogs(
        userId: String,
        startDate: Date? = null,
        endDate: Date? = null
    ): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance_logs WHERE syncStatus = :status")
    fun getBySyncStatus(status: String): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance_logs WHERE status = :status ORDER BY timestamp DESC")
    fun getByStatus(status: String): Flow<List<AttendanceEntity>>

    @Query("DELETE FROM attendance_logs")
    suspend fun deleteAll()

    @Query("UPDATE attendance_logs SET syncStatus = :newStatus WHERE id IN (:ids)")
    suspend fun updateSyncStatus(ids: List<String>, newStatus: String)
} 