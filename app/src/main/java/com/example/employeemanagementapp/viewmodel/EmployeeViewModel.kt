package com.example.employeemanagementapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeemanagementapp.data.Employee
import com.example.employeemanagementapp.data.EmployeeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmployeeViewModel(application: Application, private val userId: String) : AndroidViewModel(application) {

    private val repository = EmployeeRepository(application)
    private val _employees = MutableStateFlow<List<Employee>>(emptyList())
    val employees: StateFlow<List<Employee>> get() = _employees

    init { refreshEmployees() }

    private fun refreshEmployees() {
        viewModelScope.launch {
            _employees.value = repository.getAllEmployees(userId)
        }
    }

    fun addEmployee(employee: Employee) {
        if(userId == "GUEST") return // guest cannot add
        viewModelScope.launch {
            employee.userId = userId
            repository.addEmployee(employee)
            refreshEmployees()
        }
    }

    fun updateEmployee(employee: Employee) {
        if(userId == "GUEST") return
        viewModelScope.launch {
            repository.updateEmployee(employee)
            refreshEmployees()
        }
    }

    fun deleteEmployee(employee: Employee) {
        if(userId == "GUEST") return
        viewModelScope.launch {
            repository.deleteEmployee(employee)
            refreshEmployees()
        }
    }

    suspend fun getEmployeeById(id: Int): Employee? {
        return repository.getById(id, userId)
    }
}

