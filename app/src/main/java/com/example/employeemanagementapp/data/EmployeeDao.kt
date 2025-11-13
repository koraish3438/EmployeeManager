package com.example.employeemanagementapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.LiveData

@Dao
interface EmployeeDao {

    // IMPORTANT FIX: Returns Flow<List<Employee>> for real-time list updates in ViewModel.
    @Query("SELECT * FROM employees WHERE userId = :userId ORDER BY name ASC")
    fun getAllForUser(userId: String): Flow<List<Employee>>

    @Query("SELECT * FROM employees WHERE (name LIKE '%' || :query || '%' OR department LIKE '%' || :query || '%') AND userId = :userId ORDER BY name ASC")
    fun search(query: String, userId: String): LiveData<List<Employee>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employee: Employee): Long

    @Update
    suspend fun update(employee: Employee)

    @Delete
    suspend fun delete(employee: Employee)

    @Query("SELECT * FROM employees WHERE id = :id AND userId = :userId LIMIT 1")
    suspend fun getByIdForUser(id: Int, userId: String): Employee?
}