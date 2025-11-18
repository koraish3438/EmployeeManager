package com.example.employeemanagementapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "departments")
data class Department(
    @PrimaryKey val id: String,
    val name: String,
    val iconResId: Int, 
    val techieCount: Int         // Optional
)