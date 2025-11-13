package com.example.employeemanagementapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.employeemanagementapp.R
import com.example.employeemanagementapp.data.Employee
import com.example.employeemanagementapp.databinding.ActivityEmployeeDetailBinding
import com.example.employeemanagementapp.viewmodel.EmployeeViewModel
import com.example.employeemanagementapp.viewmodel.EmployeeViewModelFactory
import kotlinx.coroutines.launch

class EmployeeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeDetailBinding
    private lateinit var viewModel: EmployeeViewModel
    private var employeeId: Int = -1
    private var currentUserId: String = "GUEST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEmployeeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get IDs from intent
        employeeId = intent.getIntExtra("EMPLOYEE_ID", -1)
        currentUserId = intent.getStringExtra("USER_ID") ?: "GUEST"

        // Initialize ViewModel using the custom factory
        viewModel = ViewModelProvider(
            this,
            EmployeeViewModelFactory(application, currentUserId)
        )[EmployeeViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (employeeId != -1) {
            loadEmployeeDetails(employeeId)
        }

        binding.toBackAEP.setOnClickListener {
            // Simply finish the current activity to return to the list
            finish()
        }
    }

    private fun loadEmployeeDetails(id: Int) {
        // Fetch employee data from ViewModel
        lifecycleScope.launch {
            val employee: Employee? = viewModel.getEmployeeById(id)

            employee?.let {
                runOnUiThread {
                    bindEmployeeData(it)
                }
            }
        }
    }

    private fun bindEmployeeData(emp: Employee) {
        // Set data to TextViews
        binding.tvName.text = emp.name
        binding.tvDepartment.text = emp.department
        binding.tvEmail.text = emp.email
        binding.tvPhone.text = emp.phone

        // Load profile image
        if (!emp.imageUri.isNullOrEmpty()) {
            binding.imgProfile.load(emp.imageUri) {
                placeholder(R.drawable.outline_person_24)
                error(R.drawable.outline_person_24)
                transformations(CircleCropTransformation())
            }
        } else {
            binding.imgProfile.setImageResource(R.drawable.outline_person_24)
        }
    }
}