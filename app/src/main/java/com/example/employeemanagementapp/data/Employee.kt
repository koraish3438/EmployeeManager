package com.example.employeemanagementapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey val id: String, 
    val name: String,
    val role: String,
    val departmentId: String,
    val email: String,
    val phone: String,
    val userId: String,
    val profileUrl: String = ""        // Optional
)