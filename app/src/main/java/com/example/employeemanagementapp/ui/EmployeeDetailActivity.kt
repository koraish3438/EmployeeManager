package com.example.employeemanagementapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.example.employeemanagementapp.R
import com.example.employeemanagementapp.data.Employee
import com.example.employeemanagementapp.databinding.ActivityEmployeeDetailBinding
import com.example.employeemanagementapp.viewmodel.EmployeeViewModel
import com.example.employeemanagementapp.viewmodel.EmployeeViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmployeeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeDetailBinding
    private lateinit var viewModel: EmployeeViewModel
    private var employeeId: String? = null  // Pass employee ID via intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get employee ID from intent
        employeeId = intent.getStringExtra("EMPLOYEE_ID")
        val currentUserId = intent.getStringExtra("USER_ID") ?: "GUEST"

        // Initialize ViewModel
        val factory = EmployeeViewModelFactory(application, currentUserId)
        viewModel = ViewModelProvider(this, factory)[EmployeeViewModel::class.java]

        setupUI()
        loadEmployeeData()
    }

    private fun setupUI() {
        // Back button
        binding.imgBack.setOnClickListener {
            finish()
        }

        // Add button → open AddEditEmployeeActivity
        binding.imgAdd.setOnClickListener {
            val intent = Intent(this, AddEditEmployeeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadEmployeeData() {
        if (employeeId == null) return

        CoroutineScope(Dispatchers.Main).launch {
            val employee: Employee? = viewModel.getEmployeeById(employeeId!!.toInt())
            employee?.let { showEmployeeDetails(it) }
        }
    }

    private fun showEmployeeDetails(employee: Employee) {
        // Profile Image
        if (employee.profileUrl.isNotEmpty()) {
            binding.imgProfileDetails.load(employee.profileUrl) {
                placeholder(R.drawable.outline_person_24)
                error(R.drawable.outline_person_24)
                transformations(CircleCropTransformation())
            }
        } else {
            binding.imgProfileDetails.setImageResource(R.drawable.outline_person_24)
        }

        // Name & Role/Department
        binding.tvNameDetails.text = employee.name
        binding.tvRoleDetails.text = "${employee.role} | ${employee.departmentId}"

        // Stats → For now, dummy numbers
        binding.statLikes.findViewById<com.google.android.material.textview.MaterialTextView>(R.id.tvStatValue)
            .text = "24"
        binding.statThanks.findViewById<com.google.android.material.textview.MaterialTextView>(R.id.tvStatValue)
            .text = "12"
        binding.statCredits.findViewById<com.google.android.material.textview.MaterialTextView>(R.id.tvStatValue)
            .text = "5"

        // Last Updates → For now static text from XML, can be dynamic later
        binding.cardGeneral.findViewById<com.google.android.material.textview.MaterialTextView>(R.id.tvCardContent)
            .text = "Great work meeting newcomers yesterday."
        binding.cardAttitude.findViewById<com.google.android.material.textview.MaterialTextView>(R.id.tvCardContent)
            .text = "Helped team members on project last week."
    }
}
