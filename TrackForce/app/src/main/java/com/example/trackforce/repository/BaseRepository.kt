package com.example.trackforce.repository

import com.example.trackforce.utils.Resource
import kotlinx.coroutines.flow.Flow

interface BaseRepository<T> {
    suspend fun create(item: T): Resource<T>
    suspend fun update(item: T): Resource<T>
    suspend fun delete(id: String): Resource<Boolean>
    suspend fun get(id: String): Resource<T>
    fun getAll(): Flow<Resource<List<T>>>
    fun getStream(id: String): Flow<Resource<T>>
} 