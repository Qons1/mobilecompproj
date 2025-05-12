package com.example.trackforce.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.trackforce.R
import com.example.trackforce.viewmodels.ReportsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ReportsActivity : AppCompatActivity() {
    private val viewModel: ReportsViewModel by viewModels()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var selectedDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        val etDate = findViewById<EditText>(R.id.etDate)
        val etEmployee = findViewById<EditText>(R.id.etEmployee)
        val btnAttendanceReport = findViewById<Button>(R.id.btnAttendanceReport)
        val btnTaskReport = findViewById<Button>(R.id.btnTaskReport)
        val btnGenerate = findViewById<Button>(R.id.btnGenerate)

        etDate.setOnClickListener {
            showDatePicker(etDate)
        }

        btnAttendanceReport.setOnClickListener {
            val dateStr = etDate.text.toString()
            val employeeId = etEmployee.text.toString()

            if (dateStr.isEmpty() || employeeId.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.getAttendanceReport(dateStr, employeeId) { logs ->
                // Handle attendance logs
                if (logs.isEmpty()) {
                    Toast.makeText(this, "No attendance records found", Toast.LENGTH_SHORT).show()
                } else {
                    // TODO: Display attendance logs
                    Toast.makeText(this, "Found ${logs.size} attendance records", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnTaskReport.setOnClickListener {
            val employeeId = etEmployee.text.toString()

            if (employeeId.isEmpty()) {
                Toast.makeText(this, "Please enter employee ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.getTaskReport(employeeId) { tasks ->
                // Handle tasks
                if (tasks.isEmpty()) {
                    Toast.makeText(this, "No tasks found", Toast.LENGTH_SHORT).show()
                } else {
                    // TODO: Display tasks
                    Toast.makeText(this, "Found ${tasks.size} tasks", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnGenerate.setOnClickListener {
            val employeeId = etEmployee.text.toString().takeIf { it.isNotEmpty() }
            selectedDate?.let { date ->
                val endDate = Calendar.getInstance().apply {
                    time = date
                    add(Calendar.DAY_OF_MONTH, 1)
                }.time
                viewModel.generateReport(date.time, endDate.time, employeeId)
            } ?: run {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker(etDate: EditText) {
        val calendar = Calendar.getInstance()
        
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.time
                etDate.setText(dateFormat.format(selectedDate))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}

