package com.example.employeemanagementapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.employeemanagementapp.R
import com.example.employeemanagementapp.data.Employee
import com.example.employeemanagementapp.databinding.ActivityAddEditEmployeeBinding
import com.example.employeemanagementapp.viewmodel.EmployeeViewModel
import com.example.employeemanagementapp.viewmodel.EmployeeViewModelFactory // Import the custom factory
import kotlinx.coroutines.launch

class AddEditEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditEmployeeBinding
    private lateinit var viewModel: EmployeeViewModel
    private var editingEmployeeId: Int? = null
    private var currentEmployee: Employee? = null
    private lateinit var currentUserId: String // To hold the user ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // **UPDATE 1: Get currentUserId from Intent**
        currentUserId = intent.getStringExtra("CURRENT_USER_ID") ?: "GUEST"

        // **UPDATE 2: Initialize ViewModel using the custom factory**
        // This prevents the crash caused by the ViewModel requiring a 'userId' argument.
        viewModel = ViewModelProvider(
            this,
            EmployeeViewModelFactory(application, currentUserId)
        )[EmployeeViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editingEmployeeId = intent.getIntExtra("EMPLOYEE_ID", -1).takeIf { it != -1 }

        // If editing, load employee data
        editingEmployeeId?.let { id ->
            lifecycleScope.launch {
                // Fetch employee data by ID
                val emp = viewModel.getEmployeeById(id)
                emp?.let {
                    currentEmployee = it
                    binding.etName.setText(it.name)
                    binding.etDepartment.setText(it.department)
                    binding.etEmail.setText(it.email)
                    binding.etPhone.setText(it.phone)
                    // imageUri handling if needed
                }
            }
        }

        binding.backAEP.setOnClickListener {
            startActivity(Intent(this, EmployeeListActivity::class.java))
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val department = binding.etDepartment.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val employee = if (currentEmployee != null) {
                // Update existing employee object
                currentEmployee!!.apply {
                    this.name = name
                    this.department = department
                    this.email = email
                    this.phone = phone
                }
            } else {
                // Create new employee object
                Employee(
                    name = name,
                    department = department,
                    email = email,
                    phone = phone,
                    imageUri = null,
                    // **UPDATE 3: Set userId for the new employee**
                    userId = currentUserId
                )
            }

            if (currentEmployee != null) {
                viewModel.updateEmployee(employee)
                Toast.makeText(this, "Employee updated", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addEmployee(employee)
                Toast.makeText(this, "Employee added", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}