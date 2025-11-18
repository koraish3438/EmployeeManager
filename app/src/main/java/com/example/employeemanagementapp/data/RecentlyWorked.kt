package com.example.employeemanagementapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recently_worked")
data class RecentlyWorked(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employeeId: String,
    val lastWorkedDate: String,
    val employeeName: String? = null,
    val employeeRole: String? = null,
    val employeeImageUri: String? = null,
    val employeeEmail: String? = null,
    val employeePhoneNumber: String? = null
)
