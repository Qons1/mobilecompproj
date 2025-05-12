package com.example.trackforce.repository

import com.example.trackforce.data.models.AttendanceLog
import com.example.trackforce.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : BaseRepository<AttendanceLog> {
    
    private val logsCollection = firestore.collection("attendance_logs")

    override suspend fun create(item: AttendanceLog): Resource<AttendanceLog> {
        return try {
            val docRef = logsCollection.document()
            val itemWithId = item.copy(id = docRef.id)
            docRef.set(itemWithId).await()
            Resource.success(itemWithId)
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override suspend fun update(item: AttendanceLog): Resource<AttendanceLog> {
        return try {
            logsCollection.document(item.id).set(item).await()
            Resource.success(item)
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override suspend fun delete(id: String): Resource<Boolean> {
        return try {
            logsCollection.document(id).delete().await()
            Resource.success(true)
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override suspend fun get(id: String): Resource<AttendanceLog> {
        return try {
            val document = logsCollection.document(id).get().await()
            document.toObject<AttendanceLog>()?.let {
                Resource.success(it)
            } ?: Resource.error(message = "Log not found")
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override fun getAll(): Flow<Resource<List<AttendanceLog>>> {
        return logsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                try {
                    Resource.success(
                        snapshot.documents.mapNotNull { it.toObject<AttendanceLog>() }
                    )
                } catch (e: Exception) {
                    Resource.error(e)
                }
            }
    }

    override fun getStream(id: String): Flow<Resource<AttendanceLog>> {
        return logsCollection.document(id).snapshots()
            .map { snapshot ->
                try {
                    snapshot.toObject<AttendanceLog>()?.let {
                        Resource.success(it)
                    } ?: Resource.error(message = "Log not found")
                } catch (e: Exception) {
                    Resource.error(e)
                }
            }
    }

    fun getUserLogs(
        userId: String,
        startDate: Date? = null,
        endDate: Date? = null
    ): Flow<Resource<List<AttendanceLog>>> {
        var query = logsCollection
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)

        if (startDate != null) {
            query = query.whereGreaterThanOrEqualTo("timestamp", startDate)
        }
        if (endDate != null) {
            query = query.whereLessThanOrEqualTo("timestamp", endDate)
        }

        return query.snapshots()
            .map { snapshot ->
                try {
                    Resource.success(
                        snapshot.documents.mapNotNull { it.toObject<AttendanceLog>() }
                    )
                } catch (e: Exception) {
                    Resource.error(e)
                }
            }
    }

    fun getPendingVerification(): Flow<Resource<List<AttendanceLog>>> {
        return logsCollection
            .whereEqualTo("status", AttendanceLog.STATUS_PENDING)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                try {
                    Resource.success(
                        snapshot.documents.mapNotNull { it.toObject<AttendanceLog>() }
                    )
                } catch (e: Exception) {
                    Resource.error(e)
                }
            }
    }
} 