package com.example.employeemanagementapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentlyWorkedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recent: RecentlyWorked)

    @Update
    suspend fun update(recent: RecentlyWorked)

    @Delete
    suspend fun delete(recent: RecentlyWorked)

    @Query("SELECT * FROM recently_worked ORDER BY lastWorkedDate DESC")
    fun getAllRecentlyWorked(): Flow<List<RecentlyWorked>>  

    @Query("SELECT * FROM recently_worked WHERE employeeId = :empId LIMIT 1")
    suspend fun getByEmployeeId(empId: String): RecentlyWorked?
}

