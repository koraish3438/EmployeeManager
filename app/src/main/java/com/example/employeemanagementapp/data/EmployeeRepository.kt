package com.example.employeemanagementapp.data

import android.app.Application
import kotlinx.coroutines.flow.Flow

class EmployeeRepository(application: Application) {

    private val db = EmployeeDatabase.getDatabase(application)
    private val employeeDao = db.employeeDao()
    private val departmentDao = db.departmentDao()
    private val recentlyWorkedDao = db.recentlyWorkedDao()

    // ===========================
    // Employee functions
    // ===========================
    fun getAllEmployees(userId: String): Flow<List<Employee>> = employeeDao.getAllForUser(userId)

    suspend fun addEmployee(employee: Employee) = employeeDao.insert(employee)

    suspend fun updateEmployee(employee: Employee) = employeeDao.update(employee)

    suspend fun deleteEmployee(employee: Employee) = employeeDao.delete(employee)

    // **Fix:** Use String ID instead of Int
    suspend fun getEmployeeById(id: String, userId: String): Employee? =
        employeeDao.getByIdForUser(id, userId)

//    fun searchEmployees(query: String, userId: String): Flow<List<Employee>> =
//        employeeDao.search(query, userId)

    // ===========================
    // Department functions
    // ===========================
    fun getAllDepartments(): Flow<List<Department>> = departmentDao.getAllDepartments()

    suspend fun addDepartment(department: Department) = departmentDao.insert(department)

    suspend fun updateDepartment(department: Department) = departmentDao.update(department)

    suspend fun deleteDepartment(department: Department) = departmentDao.delete(department)

    // ===========================
    // RecentlyWorked functions
    // ===========================
    fun getAllRecentlyWorked(): Flow<List<RecentlyWorked>> = recentlyWorkedDao.getAllRecentlyWorked()

    suspend fun addRecentlyWorked(recent: RecentlyWorked) = recentlyWorkedDao.insert(recent)

    suspend fun updateRecentlyWorked(recent: RecentlyWorked) = recentlyWorkedDao.update(recent)

    suspend fun deleteRecentlyWorked(recent: RecentlyWorked) = recentlyWorkedDao.delete(recent)
}
