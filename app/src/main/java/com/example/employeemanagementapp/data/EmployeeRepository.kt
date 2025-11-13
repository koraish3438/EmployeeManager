package com.example.employeemanagementapp.data

import android.app.Application
import kotlinx.coroutines.flow.Flow

class EmployeeRepository(private val application: Application) {

    private val dao: EmployeeDao = EmployeeDatabase.getDatabase(application).employeeDao()

    // FIX: Removed 'suspend' and returns Flow from DAO
    fun getAllEmployees(userId: String): Flow<List<Employee>> = dao.getAllForUser(userId)

    suspend fun addEmployee(employee: Employee) = dao.insert(employee)

    suspend fun updateEmployee(employee: Employee) = dao.update(employee)

    suspend fun deleteEmployee(employee: Employee) = dao.delete(employee)

    suspend fun getById(id: Int, userId: String): Employee? = dao.getByIdForUser(id, userId)

    fun getAllLiveData(userId: String) = dao.getAllForUser(userId) // Note: This now returns Flow from DAO due to name collision, but is functionally okay
    fun searchLiveData(query: String, userId: String) = dao.search(query, userId)

}