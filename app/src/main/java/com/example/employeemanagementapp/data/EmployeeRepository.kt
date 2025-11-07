package com.example.employeemanagementapp.data

import android.app.Application

class EmployeeRepository(application: Application) {

    private val dao: EmployeeDao = EmployeeDatabase.getDatabase(application).employeeDao()

    // Return a plain list (suspend) for ViewModel to populate StateFlow
    suspend fun getAllEmployees(): List<Employee> = dao.getAllList()

    suspend fun addEmployee(employee: Employee): Long = dao.insert(employee)

    suspend fun updateEmployee(employee: Employee) = dao.update(employee)

    suspend fun deleteEmployee(employee: Employee) = dao.delete(employee)

    suspend fun getById(id: Int): Employee? = dao.getById(id)

    // If other parts of app want LiveData search/all, we keep DAO LiveData methods available
    fun getAllLiveData() = dao.getAll()
    fun searchLiveData(query: String) = dao.search(query)
}
