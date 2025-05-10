package com.example.trackforce.viewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class AttendanceViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    fun clockInOut(userId: String, type: String, lat: Double, lon: Double, onResult: (Boolean, String?) -> Unit) {
        val log = AttendanceLog(userId, System.currentTimeMillis(), type, lat, lon)
        db.collection("attendance").add(log)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    fun getAttendanceLogs(userId: String, onResult: (List<AttendanceLog>) -> Unit) {
        db.collection("attendance")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val logs = result.toObjects(AttendanceLog::class.java)
                onResult(logs)
            }
    }
}

