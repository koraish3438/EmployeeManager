package com.example.employeemanagementapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeemanagementapp.data.Employee
import com.example.employeemanagementapp.data.EmployeeDao
import com.example.employeemanagementapp.data.EmployeeDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmployeeViewModel(
    application: Application,
    private val currentUserId: String
) : AndroidViewModel(application) {

    private val dao: EmployeeDao = EmployeeDatabase.getDatabase(application).employeeDao()

    private val _employees = MutableStateFlow<List<Employee>>(emptyList())
    val employees: StateFlow<List<Employee>> = _employees

    init {
        loadEmployees()
    }

    private fun loadEmployees() {
        viewModelScope.launch {
            dao.getAllForUser(currentUserId).collect { list ->
                _employees.value = list
            }
        }
    }


    fun addEmployee(emp: Employee) = viewModelScope.launch {
        dao.insert(emp)
        loadEmployees()
    }

    fun updateEmployee(emp: Employee) = viewModelScope.launch {
        dao.update(emp)
        loadEmployees()
    }

    fun deleteEmployee(emp: Employee) = viewModelScope.launch {
        dao.delete(emp)
        loadEmployees()
    }

    // Suspend function to get employee by String ID
    suspend fun getEmployeeById(id: String): Employee? {
        return dao.getEmployeeById(id)
    }
}
