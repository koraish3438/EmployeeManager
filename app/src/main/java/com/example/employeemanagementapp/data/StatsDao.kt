package com.example.employeemanagementapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface StatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stats: EmployeeStats)

    @Update
    suspend fun update(stats: EmployeeStats)

    @Query("SELECT * FROM employee_stats WHERE employeeId = :empId")
    fun getStatsByEmployee(empId: Int): LiveData<EmployeeStats>
}