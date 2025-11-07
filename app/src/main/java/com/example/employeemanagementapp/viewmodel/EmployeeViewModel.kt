package com.example.employeemanagementapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeemanagementapp.data.Employee
import com.example.employeemanagementapp.data.EmployeeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmployeeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EmployeeRepository(application)

    private val _employees = MutableStateFlow<List<Employee>>(emptyList())
    val employees: StateFlow<List<Employee>> get() = _employees

    init {
        // load initial data
        refreshEmployees()
    }

    private fun refreshEmployees() {
        viewModelScope.launch {
            try {
                val list = repository.getAllEmployees()
                _employees.value = list
            } catch (e: Exception) {
                // optionally log error
                _employees.value = emptyList()
            }
        }
    }

    fun addEmployee(employee: Employee) {
        viewModelScope.launch {
            repository.addEmployee(employee)
            refreshEmployees()
        }
    }

    fun updateEmployee(employee: Employee) {
        viewModelScope.launch {
            repository.updateEmployee(employee)
            refreshEmployees()
        }
    }

    fun deleteEmployee(employee: Employee) {
        viewModelScope.launch {
            repository.deleteEmployee(employee)
            refreshEmployees()
        }
    }

    // Suspend helper to fetch single employee directly (call from lifecycleScope)
    suspend fun getEmployeeById(id: Int): Employee? {
        return repository.getById(id)
    }
}
