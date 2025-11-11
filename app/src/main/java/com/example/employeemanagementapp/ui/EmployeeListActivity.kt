package com.example.employeemanagementapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeemanagementapp.R
import com.example.employeemanagementapp.data.Employee
import com.example.employeemanagementapp.databinding.ActivityEmployeeListBinding
import com.example.employeemanagementapp.registration.WelcomeActivity
import com.example.employeemanagementapp.viewmodel.EmployeeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EmployeeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeListBinding
    private lateinit var adapter: EmployeeAdapter
    private lateinit var viewModel: EmployeeViewModel
    private var userName: String = "Guest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve userName from intent or SharedPreferences
        userName = intent.getStringExtra("userName") ?: run {
            val sharedPref = getSharedPreferences("MyAppPref", MODE_PRIVATE)
            sharedPref.getString("name", "Guest") ?: "Guest"
        }

        // Update Drawer header TextView
        val headerView = binding.navigationView.getHeaderView(0)
        val tvUserName = headerView.findViewById<TextView>(R.id.tvUserName)
        tvUserName.text = userName

        // ViewModel
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[EmployeeViewModel::class.java]

        // Toolbar
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24) // hamburger icon

        // Navigation drawer menu clicks
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> { /* TODO */ }
                R.id.menu_profile -> { /* TODO */ }
                R.id.menu_settings -> { /* TODO */ }
                R.id.menu_logout -> { logout() }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // RecyclerView
        adapter = EmployeeAdapter(
            onItemClick = { employee -> openEmployeeDetail(employee) },
            onEditClick = { employee -> editEmployee(employee) },
            onDeleteClick = { employee -> deleteEmployee(employee) }
        )
        binding.rvEmployees.layoutManager = LinearLayoutManager(this)
        binding.rvEmployees.adapter = adapter

        // Floating action button
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditEmployeeActivity::class.java)
            startActivity(intent)
        }

        // Observe StateFlow
        lifecycleScope.launch {
            viewModel.employees.collectLatest { list ->
                adapter.submitList(list)
            }
        }
    }

    // Handle hamburger click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openEmployeeDetail(employee: Employee) {
        val intent = Intent(this, EmployeeDetailActivity::class.java)
        intent.putExtra("EMPLOYEE_ID", employee.id)
        startActivity(intent)
    }

    private fun editEmployee(employee: Employee) {
        val intent = Intent(this, AddEditEmployeeActivity::class.java)
        intent.putExtra("EMPLOYEE_ID", employee.id)
        startActivity(intent)
    }

    private fun deleteEmployee(employee: Employee) {
        viewModel.deleteEmployee(employee)
    }

    private fun logout() {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
