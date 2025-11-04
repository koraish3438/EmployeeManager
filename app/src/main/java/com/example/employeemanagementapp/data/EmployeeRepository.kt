package com.example.employeemanagementapp.data

import androidx.lifecycle.LiveData


class EmployeeRepository(private val dao: EmployeeDao) {


    val allEmployees: LiveData<List<Employee>> = dao.getAll()


    fun search(query: String): LiveData<List<Employee>> = dao.search(query)


    suspend fun insert(employee: Employee) = dao.insert(employee)


    suspend fun update(employee: Employee) = dao.update(employee)


    suspend fun delete(employee: Employee) = dao.delete(employee)


    suspend fun getById(id: Int) = dao.getById(id)
}