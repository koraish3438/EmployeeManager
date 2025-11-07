package com.example.employeemanagementapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EmployeeDao {

    // Existing LiveData query (can be used by UI if desired)
    @Query("SELECT * FROM employees ORDER BY name ASC")
    fun getAll(): LiveData<List<Employee>>

    // New suspend function to get list (used by repository for synchronous suspend access)
    @Query("SELECT * FROM employees ORDER BY name ASC")
    suspend fun getAllList(): List<Employee>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employee: Employee): Long

    @Update
    suspend fun update(employee: Employee)

    @Delete
    suspend fun delete(employee: Employee)

    @Query("SELECT * FROM employees WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Employee?

    @Query("SELECT * FROM employees WHERE name LIKE '%' || :query || '%' OR department LIKE '%' || :query || '%' ORDER BY name ASC")
    fun search(query: String): LiveData<List<Employee>>
}