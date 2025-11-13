package com.example.employeemanagementapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var userId: String = "",
    var name: String = "",
    var department: String = "",
    var email: String = "",
    var phone: String = "",
    var imageUri: String? = null
)