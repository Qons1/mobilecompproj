package com.example.trackforce.data.local.dao

import androidx.room.*
import com.example.trackforce.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT * FROM users WHERE uid = :id")
    suspend fun get(id: String): UserEntity?

    @Query("SELECT * FROM users WHERE uid = :id")
    fun getStream(id: String): Flow<UserEntity?>

    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE role = :role")
    fun getByRole(role: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE isActive = 1")
    fun getActive(): Flow<List<UserEntity>>

    @Query("DELETE FROM users")
    suspend fun deleteAll()
} 