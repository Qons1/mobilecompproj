package com.example.trackforce.viewmodels

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import java.util.Date
import com.example.trackforce.data.models.AttendanceLog

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private fun getNetworkInfo(): Pair<Boolean, String> {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        
        val isConnected = capabilities != null
        val networkType = when {
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> "WIFI"
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "CELLULAR"
            else -> "NONE"
        }
        
        return Pair(isConnected, networkType)
    }

    private fun getBatteryInfo(): Int {
        val batteryStatus = context.registerReceiver(null, 
            IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        
        return batteryStatus?.let { intent ->
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            (level * 100 / scale.toFloat()).toInt()
        } ?: 0
    }

    fun clockInOut(
        latitude: Double,
        longitude: Double,
        accuracy: Float,
        callback: (Boolean, String) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return callback(false, "User not logged in")
        val (networkConnected, networkType) = getNetworkInfo()
        
        val log = AttendanceLog(
            userId = userId,
            timestamp = Date(),
            latitude = latitude,
            longitude = longitude,
            locationAccuracy = accuracy,
            deviceManufacturer = Build.MANUFACTURER,
            deviceModel = Build.MODEL,
            deviceOsVersion = Build.VERSION.RELEASE,
            appVersion = "1.0.0", // Hardcoded version since BuildConfig is causing issues
            batteryLevel = getBatteryInfo(),
            networkConnected = networkConnected,
            networkType = networkType,
            networkSignalStrength = 0 // We'll skip this since it requires API 29
        )

        firestore.collection("attendance_logs")
            .add(log)
            .addOnSuccessListener {
                callback(true, "Clock operation successful")
            }
            .addOnFailureListener { e ->
                callback(false, "Error: ${e.message}")
            }
    }

    fun getLastAttendanceStatus(callback: (String?) -> Unit) {
        val userId = auth.currentUser?.uid ?: run {
            callback(null)
            return
        }

        firestore.collection("attendance_logs")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                val lastStatus = documents.firstOrNull()?.getString("type")
                callback(lastStatus)
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}

