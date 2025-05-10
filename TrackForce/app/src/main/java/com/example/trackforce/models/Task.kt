package com.example.trackforce.models

data class Task(
    val id: String = "",
    val userId: String = "",
    val description: String = "",
    val completed: Boolean = false
)

