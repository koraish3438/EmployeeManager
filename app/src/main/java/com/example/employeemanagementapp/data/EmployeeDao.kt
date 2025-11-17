package com.example.employeemanagementapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employees WHERE userId = :userId")
    fun getAllForUser(userId: String): Flow<List<Employee>>

    @Insert
    suspend fun insert(employee: Employee)

    @Update
    suspend fun update(employee: Employee)

    @Delete
    suspend fun delete(employee: Employee)

    @Query("SELECT * FROM employees WHERE id = :id AND userId = :userId LIMIT 1")
    suspend fun getByIdForUser(id: Int, userId: String): Employee?

    @Query("SELECT * FROM employees WHERE name LIKE '%' || :query || '%' AND userId = :userId")
    fun search(query: String, userId: String): Flow<List<Employee>>
}

