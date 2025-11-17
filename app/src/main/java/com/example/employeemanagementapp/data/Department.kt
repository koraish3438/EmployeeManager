package com.example.employeemanagementapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "departments")
data class Department(
    @PrimaryKey val id: String,
    val name: String,
    val imageUri: String? = null,
    val iconUrl: String = ""           // Optional
)