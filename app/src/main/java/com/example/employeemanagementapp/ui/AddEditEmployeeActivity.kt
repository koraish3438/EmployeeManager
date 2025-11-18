package com.example.employeemanagementapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.employeemanagementapp.data.Employee
import com.example.employeemanagementapp.databinding.ActivityAddEditEmployeeBinding
import com.example.employeemanagementapp.viewmodel.EmployeeViewModel
import com.example.employeemanagementapp.viewmodel.EmployeeViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.UUID

class AddEditEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditEmployeeBinding
    private lateinit var viewModel: EmployeeViewModel

    private var editingEmployeeId: String? = null
    private var currentUserId: String = "GUEST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUserId = intent.getStringExtra("CURRENT_USER_ID") ?: "GUEST"

        viewModel = ViewModelProvider(
            this,
            EmployeeViewModelFactory(application, currentUserId)
        )[EmployeeViewModel::class.java]

        // Check if editing
        editingEmployeeId = intent.getStringExtra("EMPLOYEE_ID")
        editingEmployeeId?.let { id ->
            lifecycleScope.launch {
                val emp = viewModel.getEmployeeById(id)
                emp?.let { populateFields(it) }
            }
        }

        binding.btnSave.setOnClickListener { saveEmployee() }
        binding.backAEP.setOnClickListener { finish() }
    }

    private fun populateFields(emp: Employee) {
        binding.etName.setText(emp.name)
        binding.etDepartment.setText(emp.departmentId)
        binding.etEmail.setText(emp.email)
        binding.etPhone.setText(emp.phone)
        binding.etRole.setText(emp.role)
    }

    private fun saveEmployee() {
        val name = binding.etName.text.toString().trim()
        val department = binding.etDepartment.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val role = binding.etRole.text.toString().trim()

        if (name.isEmpty() || department.isEmpty() || email.isEmpty() || phone.isEmpty() || role.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        val newId = editingEmployeeId ?: UUID.randomUUID().toString()

        val employee = Employee(
            id = newId,
            name = name,
            role = role,
            departmentId = department,
            email = email,
            phone = phone,
            userId = currentUserId
        )

        if (editingEmployeeId != null) {
            // Updating an existing employee
            viewModel.updateEmployee(employee)
            Toast.makeText(this, "Employee updated", Toast.LENGTH_SHORT).show()

            // For updates, we don't usually log to "Recently Worked" via Intent result,
            // but we still need to finish the activity.
            setResult(Activity.RESULT_CANCELED)
            finish()
        } else {
            // Adding a new employee
            viewModel.addEmployee(employee)
            Toast.makeText(this, "Employee added", Toast.LENGTH_SHORT).show()

            // Pass the new employee details back to EmployeeListActivity
            // to update the "Recently Worked" list.
            val resultIntent = Intent().apply {
                putExtra("NEW_EMPLOYEE_ID", employee.id)
                putExtra("NEW_EMPLOYEE_NAME", employee.name)
                putExtra("NEW_EMPLOYEE_ROLE", employee.role)
                putExtra("NEW_EMPLOYEE_PHOTO", employee.profileUrl) // Assuming Employee has profileUrl field
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}