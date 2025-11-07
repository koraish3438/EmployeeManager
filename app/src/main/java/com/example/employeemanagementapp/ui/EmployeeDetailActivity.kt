package com.example.employeemanagementapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import coil.transform.CircleCropTransformation
import com.example.employeemanagementapp.R
import com.example.employeemanagementapp.data.Employee
import com.example.employeemanagementapp.data.EmployeeDatabase
import com.example.employeemanagementapp.databinding.ActivityEmployeeDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmployeeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeDetailBinding
    private var employeeId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEmployeeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // WindowInsets padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get Employee ID from intent
        employeeId = intent.getIntExtra("EMPLOYEE_ID", -1)
        if (employeeId != -1) {
            loadEmployeeDetails(employeeId)
        }
    }

    private fun loadEmployeeDetails(id: Int) {
        val dao = EmployeeDatabase.getDatabase(applicationContext).employeeDao()
        CoroutineScope(Dispatchers.IO).launch {
            val employee: Employee? = dao.getById(id)
            employee?.let {
                runOnUiThread {
                    bindEmployeeData(it)
                }
            }
        }
    }

    private fun bindEmployeeData(emp: Employee) {
        binding.tvName.text = emp.name
        binding.tvDepartment.text = emp.department
        binding.tvEmail.text = emp.email
        binding.tvPhone.text = emp.phone

        // Profile image
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
