package com.example.trackforce.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.trackforce.R
import com.example.trackforce.viewmodels.AttendanceViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class ClockInOutActivity : AppCompatActivity() {
    private lateinit var viewModel: AttendanceViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock_in_out)
        viewModel = ViewModelProvider(this).get(AttendanceViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val btnClockInOut = findViewById<Button>(R.id.btnClockInOut)
        // Setup location and employee list

        btnClockInOut.setOnClickListener {
            // Get current location, then:
            // viewModel.clockInOut(userId, "in" or "out", lat, lon) { ... }
        }
    }

    private fun checkLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 44)
        }
    }

    private fun getLocation() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    // Use location as needed
                }
            }
        }, Looper.getMainLooper())
    }
}

