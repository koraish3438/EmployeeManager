package com.example.employeemanagementapp.data

import android.app.Application

class EmployeeRepository(private val application: Application) {

    private val dao: EmployeeDao = EmployeeDatabase.getDatabase(application).employeeDao()

    suspend fun getAllEmployees(userId: String): List<Employee> = dao.getAllListForUser(userId)

    suspend fun addEmployee(employee: Employee) = dao.insert(employee)

    suspend fun updateEmployee(employee: Employee) = dao.update(employee)

    suspend fun deleteEmployee(employee: Employee) = dao.delete(employee)

    suspend fun getById(id: Int, userId: String): Employee? = dao.getByIdForUser(id, userId)

    fun getAllLiveData(userId: String) = dao.getAllForUser(userId)
    fun searchLiveData(query: String, userId: String) = dao.search(query, userId)
}
