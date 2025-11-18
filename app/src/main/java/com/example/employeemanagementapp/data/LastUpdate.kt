package com.example.employeemanagementapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_updates")
data class LastUpdate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employeeId: String,
    val title: String,
    val description: String
)
