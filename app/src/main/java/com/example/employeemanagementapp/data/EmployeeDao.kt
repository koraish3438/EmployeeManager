package com.example.employeemanagementapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employees WHERE id = :id LIMIT 1")
    suspend fun getEmployeeById(id: String): Employee?

    // **Fix:** return Flow instead of List
    @Query("SELECT * FROM employees WHERE userId = :userId")
    fun getAllForUser(userId: String): Flow<List<Employee>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employee: Employee)

    @Update
    suspend fun update(employee: Employee)

    @Delete
    suspend fun delete(employee: Employee)

    @Query("SELECT * FROM employees WHERE id = :id AND userId = :userId LIMIT 1")
    suspend fun getByIdForUser(id: String, userId: String): Employee?
}
