package com.example.trackforce.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class ReportsActivity : AppCompatActivity() {
    private lateinit var viewModel: ReportsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        viewModel = ViewModelProvider(this).get(ReportsViewModel::class.java)

        // Setup date picker, employee selection, and report generation
    }
}

