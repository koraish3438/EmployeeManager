package com.example.employeemanagementapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.employeemanagementapp.data.Employee
import com.example.employeemanagementapp.data.EmployeeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EmployeeViewModel(application: Application, private val userId: String) : AndroidViewModel(application) {

    private val repository = EmployeeRepository(application)

    // Maps Repository's Flow to StateFlow with explicit type definitions
    val employees: StateFlow<List<Employee>> = if (userId != "GUEST") {
        repository.getAllEmployees(userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    } else {
        flowOf(emptyList<Employee>()).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    }

    fun addEmployee(employee: Employee) {
        if (userId == "GUEST") return
        viewModelScope.launch {
            // userId must be set at object creation, can't change val
            repository.addEmployee(employee)
        }
    }

    fun updateEmployee(employee: Employee) {
        if (userId == "GUEST") return
        viewModelScope.launch {
            repository.updateEmployee(employee)
        }
    }

    fun deleteEmployee(employee: Employee) {
        if (userId == "GUEST") return
        viewModelScope.launch {
            repository.deleteEmployee(employee)
        }
    }

    suspend fun getEmployeeById(id: Int): Employee? {
        return repository.getEmployeeById(id, userId)
    }
}
