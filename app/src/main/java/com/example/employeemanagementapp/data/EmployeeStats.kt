package com.example.employeemanagementapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee_stats")
data class EmployeeStats(
    @PrimaryKey val employeeId: String,
    val name: String,
    val role: String,
    val email: String,
    val phoneNumber: String,
    val likes: Int,
    val thanks: Int,
    val credits: Int
)