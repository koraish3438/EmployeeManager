package com.example.employeemanagementapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee_stats")
data class EmployeeStats(
    @PrimaryKey val employeeId: String,
    val likes: Int = 0,
    val thanks: Int = 0,
    val credits: Int = 0
)