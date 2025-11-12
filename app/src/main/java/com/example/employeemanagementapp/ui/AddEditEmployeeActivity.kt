package com.example.employeemanagementapp.ui

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
import com.example.employeemanagementapp.viewmodel.EmployeeViewModelFactory
import kotlinx.coroutines.launch

class AddEditEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditEmployeeBinding
    private lateinit var viewModel: EmployeeViewModel
    private var editingEmployeeId: Int? = null
    private var currentEmployee: Employee? = null
    private var currentUserId: String = "GUEST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUserId = intent.getStringExtra("USER_ID") ?: "GUEST"

        val factory = EmployeeViewModelFactory(application, currentUserId)
        viewModel = ViewModelProvider(this, factory)[EmployeeViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editingEmployeeId = intent.getIntExtra("EMPLOYEE_ID", -1).takeIf { it != -1 }

        if(editingEmployeeId != null) {
            lifecycleScope.launch {
                val emp = viewModel.getEmployeeById(editingEmployeeId!!)
                emp?.let {
                    currentEmployee = it
                    binding.etName.setText(it.name)
                    binding.etDepartment.setText(it.department)
                    binding.etEmail.setText(it.email)
                    binding.etPhone.setText(it.phone)
                }
            }
        }

        binding.btnSave.setOnClickListener {
            if(currentUserId == "GUEST") {
                Toast.makeText(this, "Login or Signup required to add/update employee", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val name = binding.etName.text.toString().trim()
            val department = binding.etDepartment.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()

            if(name.isEmpty()) { Toast.makeText(this,"Name required",Toast.LENGTH_SHORT).show(); return@setOnClickListener }

            val employee = currentEmployee?.apply {
                this.name = name
                this.department = department
                this.email = email
                this.phone = phone
            } ?: Employee(
                userId = currentUserId,
                name = name,
                department = department,
                email = email,
                phone = phone
            )

            if(currentEmployee != null) {
                viewModel.updateEmployee(employee)
                Toast.makeText(this,"Employee updated",Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addEmployee(employee)
                Toast.makeText(this,"Employee added",Toast.LENGTH_SHORT).show()
            }

            finish()
        }
    }
}
