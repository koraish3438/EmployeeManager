package com.example.employeemanagementapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
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

        val sharedPref = getSharedPreferences("MyAppPref", MODE_PRIVATE)

        // Fetch USER_ID and name
        currentUserId = intent.getStringExtra("USER_ID")
            ?: sharedPref.getString("USER_ID", "GUEST") ?: "GUEST"

        userName = intent.getStringExtra("userName")
            ?: sharedPref.getString("name", if (currentUserId == "GUEST") "Guest" else "User") ?: "Guest"

        val userEmail = sharedPref.getString("email", "")

        // Drawer header update
        val headerView = binding.navigationView.getHeaderView(0)
        val tvUserName = headerView.findViewById<TextView>(R.id.tvUserName)
        val tvUserEmail = headerView.findViewById<TextView>(R.id.tvUserEmail)
        tvUserName.text = userName
        tvUserEmail.text = if (currentUserId != "GUEST") userEmail else ""

        // ViewModel
        viewModel = ViewModelProvider(
            this,
            EmployeeViewModelFactory(application, currentUserId)
        )[EmployeeViewModel::class.java]

        // RecyclerView Adapter
        adapter = EmployeeAdapter(
            currentUserId = currentUserId,
            onItemClick = { emp -> openEmployeeDetail(emp) },
            onEditClick = { emp ->
                if (currentUserId == "GUEST") showLoginPrompt()
                else editEmployee(emp)
            },
            onDeleteClick = { emp ->
                if (currentUserId == "GUEST") showLoginPrompt()
                else deleteEmployee(emp)
            }
        )

        binding.rvEmployees.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvEmployees.adapter = adapter

        // FAB
        binding.fabAdd.setOnClickListener {
            if (currentUserId == "GUEST") showLoginPrompt()
            else {
                val intent = Intent(this, AddEditEmployeeActivity::class.java)
                intent.putExtra("USER_ID", currentUserId)
                startActivity(intent)
            }
        }

        // Observe employees
        lifecycleScope.launch {
            viewModel.employees.collectLatest { list ->
                adapter.submitList(list)
            }
        }

        // Navigation drawer
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

        // Toolbar
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
    }

    private fun showLoginPrompt() {
        Toast.makeText(
            this,
            "Please log in or sign up to perform this action",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun openEmployeeDetail(employee: Employee) {
        val intent = Intent(this, EmployeeDetailActivity::class.java)
        intent.putExtra("EMPLOYEE_ID", employee.id)
        intent.putExtra("USER_ID", currentUserId)
        startActivity(intent)
    }

    private fun editEmployee(employee: Employee) {
        val intent = Intent(this, AddEditEmployeeActivity::class.java)
        intent.putExtra("EMPLOYEE_ID", employee.id)
        intent.putExtra("USER_ID", currentUserId)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
