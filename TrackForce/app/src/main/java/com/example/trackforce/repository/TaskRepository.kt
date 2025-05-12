package com.example.trackforce.repository

import com.example.trackforce.data.models.Task
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
class TaskRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : BaseRepository<Task> {
    
    private val tasksCollection = firestore.collection("tasks")

    override suspend fun create(item: Task): Resource<Task> {
        return try {
            val docRef = tasksCollection.document()
            val itemWithId = item.copy(id = docRef.id)
            docRef.set(itemWithId).await()
            Resource.success(itemWithId)
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override suspend fun update(item: Task): Resource<Task> {
        return try {
            tasksCollection.document(item.id).set(item).await()
            Resource.success(item)
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override suspend fun delete(id: String): Resource<Boolean> {
        return try {
            tasksCollection.document(id).delete().await()
            Resource.success(true)
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override suspend fun get(id: String): Resource<Task> {
        return try {
            val document = tasksCollection.document(id).get().await()
            document.toObject<Task>()?.let {
                Resource.success(it)
            } ?: Resource.error(message = "Task not found")
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override fun getAll(): Flow<Resource<List<Task>>> {
        return tasksCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                try {
                    Resource.success(
                        snapshot.documents.mapNotNull { it.toObject<Task>() }
                    )
                } catch (e: Exception) {
                    Resource.error(e)
                }
            }
    }

    override fun getStream(id: String): Flow<Resource<Task>> {
        return tasksCollection.document(id).snapshots()
            .map { snapshot ->
                try {
                    snapshot.toObject<Task>()?.let {
                        Resource.success(it)
                    } ?: Resource.error(message = "Task not found")
                } catch (e: Exception) {
                    Resource.error(e)
                }
            }
    }

    fun getTasksByAssignee(
        userId: String,
        status: String? = null
    ): Flow<Resource<List<Task>>> {
        var query = tasksCollection
            .whereEqualTo("assignedTo", userId)
            .orderBy("dueDate", Query.Direction.ASCENDING)

        if (status != null) {
            query = query.whereEqualTo("status", status)
        }

        return query.snapshots()
            .map { snapshot ->
                try {
                    Resource.success(
                        snapshot.documents.mapNotNull { it.toObject<Task>() }
                    )
                } catch (e: Exception) {
                    Resource.error(e)
                }
            }
    }

    fun getTasksByLocation(
        latitude: Double,
        longitude: Double,
        radiusInMeters: Double
    ): Flow<Resource<List<Task>>> {
        // This is a simplified version. In a real app, you'd need to implement
        // proper geospatial queries using Firebase GeoFirestore
        return tasksCollection
            .whereNotEqualTo("locationLatitude", null)
            .whereNotEqualTo("locationLongitude", null)
            .snapshots()
            .map { snapshot ->
                try {
                    val tasks = snapshot.documents.mapNotNull { it.toObject<Task>() }
                    val nearbyTasks = tasks.filter { task ->
                        task.locationLatitude?.let { taskLat ->
                            task.locationLongitude?.let { taskLon ->
                                isWithinRadius(
                                    lat1 = latitude,
                                    lon1 = longitude,
                                    lat2 = taskLat,
                                    lon2 = taskLon,
                                    radiusInMeters = radiusInMeters
                                )
                            }
                        } ?: false
                    }
                    Resource.success(nearbyTasks)
                } catch (e: Exception) {
                    Resource.error(e)
                }
            }
    }

    private fun isWithinRadius(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
        radiusInMeters: Double
    ): Boolean {
        val earthRadius = 6371000.0 // meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distance = earthRadius * c
        return distance <= radiusInMeters
    }
} 