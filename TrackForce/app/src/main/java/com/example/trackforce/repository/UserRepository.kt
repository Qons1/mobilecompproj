package com.example.trackforce.repository

import com.example.trackforce.data.models.User
import com.example.trackforce.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : BaseRepository<User> {
    
    private val usersCollection = firestore.collection("users")

    override suspend fun create(item: User): Resource<User> {
        return try {
            usersCollection.document(item.uid).set(item).await()
            Resource.success(item)
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override suspend fun update(item: User): Resource<User> {
        return try {
            usersCollection.document(item.uid).set(item).await()
            Resource.success(item)
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override suspend fun delete(id: String): Resource<Boolean> {
        return try {
            usersCollection.document(id).delete().await()
            Resource.success(true)
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override suspend fun get(id: String): Resource<User> {
        return try {
            val document = usersCollection.document(id).get().await()
            document.toObject<User>()?.let {
                Resource.success(it)
            } ?: Resource.error(message = "User not found")
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override fun getAll(): Flow<Resource<List<User>>> {
        return usersCollection.snapshots()
            .map { snapshot ->
                try {
                    Resource.success(
                        snapshot.documents.mapNotNull { it.toObject<User>() }
                    )
                } catch (e: Exception) {
                    Resource.error(e)
                }
            }
    }

    override fun getStream(id: String): Flow<Resource<User>> {
        return usersCollection.document(id).snapshots()
            .map { snapshot ->
                try {
                    snapshot.toObject<User>()?.let {
                        Resource.success(it)
                    } ?: Resource.error(message = "User not found")
                } catch (e: Exception) {
                    Resource.error(e)
                }
            }
    }

    fun getUsersByRole(role: String): Flow<Resource<List<User>>> {
        return usersCollection
            .whereEqualTo("role", role)
            .snapshots()
            .map { snapshot ->
                try {
                    Resource.success(
                        snapshot.documents.mapNotNull { it.toObject<User>() }
                    )
                } catch (e: Exception) {
                    Resource.error(e)
                }
            }
    }
} 