package com.example.trackforce.data.local.dao

import androidx.room.*
import com.example.trackforce.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<TaskEntity>)

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun get(id: String): TaskEntity?

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getStream(id: String): Flow<TaskEntity?>

    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAll(): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM tasks 
        WHERE assignedTo = :userId 
        AND (:status IS NULL OR status = :status)
        ORDER BY dueDate ASC
    """)
    fun getAssignedTasks(
        userId: String,
        status: String? = null
    ): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM tasks 
        WHERE assignedBy = :userId 
        ORDER BY dueDate ASC
    """)
    fun getCreatedTasks(userId: String): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM tasks 
        WHERE dueDate >= :startDate 
        AND dueDate <= :endDate
        ORDER BY dueDate ASC
    """)
    fun getTasksByDateRange(
        startDate: Date,
        endDate: Date
    ): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE syncStatus = :status")
    fun getBySyncStatus(status: String): Flow<List<TaskEntity>>

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()

    @Query("UPDATE tasks SET syncStatus = :newStatus WHERE id IN (:ids)")
    suspend fun updateSyncStatus(ids: List<String>, newStatus: String)
} 