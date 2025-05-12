package com.example.trackforce.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.trackforce.data.local.dao.AttendanceDao
import com.example.trackforce.data.local.dao.TaskDao
import com.example.trackforce.data.models.Task
import com.example.trackforce.repository.AttendanceRepository
import com.example.trackforce.repository.TaskRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@HiltWorker
class DataSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val attendanceDao: AttendanceDao,
    private val taskDao: TaskDao,
    private val attendanceRepository: AttendanceRepository,
    private val taskRepository: TaskRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            // Sync attendance logs
            attendanceDao.getBySyncStatus("PENDING").first().forEach { entity ->
                try {
                    // Convert entity to model and sync
                    // Update sync status on success
                    attendanceDao.updateSyncStatus(listOf(entity.id), "SYNCED")
                } catch (e: Exception) {
                    attendanceDao.updateSyncStatus(listOf(entity.id), "ERROR")
                }
            }

            // Sync tasks
            taskDao.getBySyncStatus("PENDING").first().forEach { entity ->
                try {
                    // Convert entity to model and sync
                    // Update sync status on success
                    taskDao.updateSyncStatus(listOf(entity.id), "SYNCED")
                } catch (e: Exception) {
                    taskDao.updateSyncStatus(listOf(entity.id), "ERROR")
                }
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }
} 