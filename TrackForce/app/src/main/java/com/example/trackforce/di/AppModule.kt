package com.example.trackforce.di

import android.content.Context
import androidx.room.Room
import com.example.trackforce.data.local.TrackForceDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFunctions(): FirebaseFunctions = FirebaseFunctions.getInstance()

    @Provides
    @Singleton
    fun provideTrackForceDatabase(
        @ApplicationContext context: Context
    ): TrackForceDatabase {
        return Room.databaseBuilder(
            context,
            TrackForceDatabase::class.java,
            "trackforce.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: TrackForceDatabase) = db.userDao()

    @Provides
    @Singleton
    fun provideAttendanceDao(db: TrackForceDatabase) = db.attendanceDao()

    @Provides
    @Singleton
    fun provideTaskDao(db: TrackForceDatabase) = db.taskDao()
} 