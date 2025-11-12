package com.example.employeemanagementapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employees WHERE userId = :userId ORDER BY name ASC")
    fun getAllForUser(userId: String): LiveData<List<Employee>>

    @Query("SELECT * FROM employees WHERE userId = :userId ORDER BY name ASC")
    suspend fun getAllListForUser(userId: String): List<Employee>

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
