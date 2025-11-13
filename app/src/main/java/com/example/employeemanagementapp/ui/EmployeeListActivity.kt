package com.example.employeemanagementapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.employeemanagementapp.R
import com.example.employeemanagementapp.data.Employee
import com.example.employeemanagementapp.databinding.ActivityEmployeeListBinding
import com.example.employeemanagementapp.registration.WelcomeActivity
import com.example.employeemanagementapp.viewmodel.EmployeeViewModel
import com.example.employeemanagementapp.viewmodel.EmployeeViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EmployeeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeListBinding
    private lateinit var adapter: EmployeeAdapter
    private lateinit var viewModel: EmployeeViewModel
    private var userName: String = "Guest"
    private var currentUserId: String = "GUEST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check login state and retrieve user data
        val sharedPref = getSharedPreferences("MyAppPref", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val loggedInEmail = sharedPref.getString("currentLoggedInEmail", null)

            if (loggedInEmail != null) {
                // Retrieving name and email using the saved key
                userName = sharedPref.getString("user_${loggedInEmail}_name", "Guest") ?: "Guest"
                currentUserId = loggedInEmail
            } else {
                // Fallback to Guest mode if isLoggedIn is true but currentLoggedInEmail is missing
                userName = "Guest"
                currentUserId = "GUEST"
            }
        } else {
            // Guest mode execution
            userName = "Guest"
            currentUserId = "GUEST"
        }

        // Update Drawer header
        val headerView = binding.navigationView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.tvUserName).text = userName
        headerView.findViewById<TextView>(R.id.tvUserEmail).text =
            if (currentUserId != "GUEST") currentUserId else "GUEST"

        // ViewModel initialization using the custom factory
        viewModel = ViewModelProvider(
            this,
            EmployeeViewModelFactory(application, currentUserId)
        )[EmployeeViewModel::class.java]

        // Toolbar setup
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)

        // Navigation drawer clicks
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> { /* TODO */ }
                R.id.menu_profile -> { /* TODO */ }
                R.id.menu_settings -> { /* TODO */ }
                R.id.menu_logout -> logout()
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // RecyclerView Adapter setup
        adapter = EmployeeAdapter(
            currentUserId = currentUserId,
            onItemClick = { employee -> openEmployeeDetail(employee) },
            onEditClick = { employee -> if (currentUserId != "GUEST") editEmployee(employee) },
            onDeleteClick = { employee -> if (currentUserId != "GUEST") deleteEmployee(employee) }
        )
        binding.rvEmployees.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvEmployees.adapter = adapter

        // FAB click (Add Employee)
        if (currentUserId == "GUEST") {
            binding.fabAdd.hide()
        } else {
            binding.fabAdd.show()
            binding.fabAdd.setOnClickListener {
                // Passing currentUserId to AddEditEmployeeActivity
                val intent = Intent(this, AddEditEmployeeActivity::class.java)
                intent.putExtra("CURRENT_USER_ID", currentUserId)
                startActivity(intent)
            }
        }

        // Observe employees flow (This correctly handles live data due to ViewModel fix)
        lifecycleScope.launch {
            viewModel.employees.collectLatest { list ->
                adapter.submitList(list)
            }
        }
    }

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
        // Passing the user ID is essential for EmployeeDetailActivity
        intent.putExtra("USER_ID", currentUserId)
        startActivity(intent)
    }

    private fun editEmployee(employee: Employee) {
        val intent = Intent(this, AddEditEmployeeActivity::class.java)
        intent.putExtra("EMPLOYEE_ID", employee.id)
        // Passing currentUserId for editing
        intent.putExtra("CURRENT_USER_ID", currentUserId)
        startActivity(intent)
    }

    private fun deleteEmployee(employee: Employee) {
        viewModel.deleteEmployee(employee)
    }

    private fun logout() {
        val sharedPref = getSharedPreferences("MyAppPref", MODE_PRIVATE)
        // Clearing only the current login status keys
        sharedPref.edit().apply {
            remove("isLoggedIn")
            remove("currentLoggedInEmail")
            apply()
        }
        startActivity(
            Intent(this, WelcomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        )
        finish()
    }
}