package com.example.trackforce.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.trackforce.data.models.Task
import com.example.trackforce.data.models.AttendanceLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {
    
    fun generateReport(startDate: Long, endDate: Long, employeeId: String? = null) {
        val start = Date(startDate)
        val end = Date(endDate)
        
        if (employeeId != null) {
            getEmployeeReport(employeeId, start, end)
        } else {
            getAllEmployeesReport(start, end)
        }
    }

    private fun getEmployeeReport(employeeId: String, startDate: Date, endDate: Date) {
        firestore.collection("attendance_logs")
            .whereEqualTo("userId", employeeId)
            .whereGreaterThanOrEqualTo("timestamp", startDate)
            .whereLessThanOrEqualTo("timestamp", endDate)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val logs = documents.toObjects(AttendanceLog::class.java)
                // Process logs and generate report
            }
    }

    private fun getAllEmployeesReport(startDate: Date, endDate: Date) {
        firestore.collection("attendance_logs")
            .whereGreaterThanOrEqualTo("timestamp", startDate)
            .whereLessThanOrEqualTo("timestamp", endDate)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val logs = documents.toObjects(AttendanceLog::class.java)
                // Process logs and generate report
            }
    }

    fun getAttendanceReport(dateStr: String, employeeId: String, onResult: (List<AttendanceLog>) -> Unit) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val date = dateFormat.parse(dateStr)
            val endDate = Date(date.time + 24 * 60 * 60 * 1000) // Next day
            
            firestore.collection("attendance_logs")
                .whereEqualTo("userId", employeeId)
                .whereGreaterThanOrEqualTo("timestamp", date)
                .whereLessThanOrEqualTo("timestamp", endDate)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    val logs = documents.toObjects(AttendanceLog::class.java)
                    onResult(logs)
                }
                .addOnFailureListener {
                    onResult(emptyList())
                }
        } catch (e: Exception) {
            onResult(emptyList())
        }
    }

    fun getTaskReport(employeeId: String, onResult: (List<Task>) -> Unit) {
        firestore.collection("tasks")
            .whereEqualTo("assignedTo", employeeId)
            .orderBy("dueDate", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                val tasks = result.toObjects(Task::class.java)
                onResult(tasks)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}

