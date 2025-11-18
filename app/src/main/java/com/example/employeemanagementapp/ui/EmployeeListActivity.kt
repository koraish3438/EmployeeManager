package com.example.employeemanagementapp.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeemanagementapp.R
import com.example.employeemanagementapp.data.Department
import com.example.employeemanagementapp.data.RecentlyWorked
import com.example.employeemanagementapp.databinding.ActivityEmployeeListBinding
import com.example.employeemanagementapp.registration.WelcomeActivity
import com.example.employeemanagementapp.viewmodel.EmployeeViewModel
import com.example.employeemanagementapp.viewmodel.EmployeeViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val ITEM_SEPARATOR = "||"
private const val FIELD_SEPARATOR = "&&"
private const val NULL_PLACEHOLDER = "NULL_DATA"

object EmployeeDetailsKeys {
    const val EXTRA_EMPLOYEE_NAME = "extra_employee_name"
    const val EXTRA_EMPLOYEE_ROLE = "extra_employee_role"
    const val EXTRA_EMPLOYEE_EMAIL = "extra_employee_email"
    const val EXTRA_EMPLOYEE_PHONE = "extra_employee_phone"
}

class EmployeeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeListBinding
    private lateinit var viewModel: EmployeeViewModel

    private lateinit var departmentAdapter: DepartmentAdapter
    private lateinit var recentlyWorkedAdapter: RecentlyWorkedAdapter

    private var userName = "Guest"
    private var currentUserId = "GUEST"

    private val RECENTLY_WORKED_PREF = "RecentlyWorkedPref"
    private val RECENTLY_WORKED_KEY = "RecentEmployeesList"
    private val MAX_RECENT_COUNT = 10

    private val addEmployeeLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val employeeId = result.data?.getStringExtra("NEW_EMPLOYEE_ID")
            val employeeName = result.data?.getStringExtra("NEW_EMPLOYEE_NAME")
            val employeeRole = result.data?.getStringExtra("NEW_EMPLOYEE_ROLE")
            val employeePhotoUrl = result.data?.getStringExtra("NEW_EMPLOYEE_PHOTO")
            val employeeEmail = result.data?.getStringExtra("NEW_EMPLOYEE_EMAIL")
            val employeePhone = result.data?.getStringExtra("NEW_EMPLOYEE_PHONE")

            if (employeeId != null && employeeName != null && employeeRole != null) {
                val newRecent = RecentlyWorked(
                    employeeId = employeeId,
                    lastWorkedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                    employeeName = employeeName,
                    employeeRole = employeeRole,
                    employeeImageUri = employeePhotoUrl,
                    employeeEmail = employeeEmail,
                    employeePhoneNumber = employeePhone
                )
                saveRecentEmployee(newRecent)
                loadRecentlyWorked()
                Toast.makeText(this, "$employeeName added", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserInfo()
        setupViewModel()
        setupToolbarAndDrawer()
        setupAdapters()
        setupBottomNav()
        observeData()
        loadRecentlyWorked()
    }

    private fun loadUserInfo() {
        val pref = getSharedPreferences("MyAppPref", MODE_PRIVATE)
        val loggedIn = pref.getBoolean("isLoggedIn", false)

        if (loggedIn) {
            val email = pref.getString("currentLoggedInEmail", null)
            if (email != null) {
                currentUserId = email
                userName = pref.getString("user_${email}_name", "Guest") ?: "Guest"
            }
        }

        val header = binding.navigationView.getHeaderView(0)
        header.findViewById<TextView>(R.id.tvUserName).text = userName
        header.findViewById<TextView>(R.id.tvUserEmail).text = currentUserId
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            EmployeeViewModelFactory(application, currentUserId)
        )[EmployeeViewModel::class.java]
    }

    private fun setupToolbarAndDrawer() {
        val toolbar = Toolbar(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24)

        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_logout -> logout()
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun setupAdapters() {
        departmentAdapter = DepartmentAdapter { department ->
            Toast.makeText(this, "Clicked: ${department.name}", Toast.LENGTH_SHORT).show()
        }
        binding.rvDepartments.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvDepartments.adapter = departmentAdapter

        recentlyWorkedAdapter = RecentlyWorkedAdapter { recent ->
            val intent = Intent(this, EmployeeDetailsActivity::class.java)

            intent.putExtra(EmployeeDetailsKeys.EXTRA_EMPLOYEE_NAME, recent.employeeName)
            intent.putExtra(EmployeeDetailsKeys.EXTRA_EMPLOYEE_ROLE, recent.employeeRole)
            intent.putExtra(EmployeeDetailsKeys.EXTRA_EMPLOYEE_EMAIL, recent.employeeEmail)
            intent.putExtra(EmployeeDetailsKeys.EXTRA_EMPLOYEE_PHONE, recent.employeePhoneNumber)

            startActivity(intent)
        }
        binding.rvRecentlyWorked.layoutManager = LinearLayoutManager(this)
        binding.rvRecentlyWorked.adapter = recentlyWorkedAdapter
    }

    private fun setupBottomNav() {
        val bottomNav = binding.bottomNavigation
        val personBtn = bottomNav.getChildAt(1) as ImageButton
        personBtn.setOnClickListener {
            val intent = Intent(this, AddEditEmployeeActivity::class.java)
            intent.putExtra("CURRENT_USER_ID", currentUserId)
            addEmployeeLauncher.launch(intent)
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            val departments = listOf(
                Department(
                    id = "1",
                    name = "Development",
                    iconResId = R.drawable.jui,
                    techieCount = 88
                ),
                Department(
                    id = "2",
                    name = "Marketing",
                    iconResId = R.drawable.marketer,
                    techieCount = 45
                ),
                Department(
                    id = "3",
                    name = "HR",
                    iconResId = R.drawable.koraish1,
                    techieCount = 20
                )
            )
            departmentAdapter.submitList(departments)
        }
    }

    private fun getRecentlyWorkedList(): MutableList<RecentlyWorked> {
        val pref = getSharedPreferences(RECENTLY_WORKED_PREF, Context.MODE_PRIVATE)
        val fullString = pref.getString(RECENTLY_WORKED_KEY, null)

        if (fullString.isNullOrEmpty()) {
            return mutableListOf()
        }

        return fullString.split(ITEM_SEPARATOR)
            .mapNotNull { itemString ->
                val fields = itemString.split(FIELD_SEPARATOR)
                // fields.size needs to be 7 now (5 previous + 2 new: Email, Phone)
                if (fields.size >= 7) {
                    val employeeId = fields[0]
                    val lastWorkedDate = fields[1]
                    val employeeName = fields[2].takeIf { it != NULL_PLACEHOLDER }
                    val employeeRole = fields[3].takeIf { it != NULL_PLACEHOLDER }
                    val employeeImageUri = fields[4].takeIf { it != NULL_PLACEHOLDER }
                    val employeeEmail = fields[5].takeIf { it != NULL_PLACEHOLDER }
                    val employeePhone = fields[6].takeIf { it != NULL_PLACEHOLDER }

                    RecentlyWorked(
                        id = 0,
                        employeeId = employeeId,
                        lastWorkedDate = lastWorkedDate,
                        employeeName = employeeName,
                        employeeRole = employeeRole,
                        employeeImageUri = employeeImageUri,
                        employeeEmail = employeeEmail,
                        employeePhoneNumber = employeePhone
                    )
                } else if (fields.size >= 5) {
                    val employeeId = fields[0]
                    val lastWorkedDate = fields[1]
                    val employeeName = fields[2].takeIf { it != NULL_PLACEHOLDER }
                    val employeeRole = fields[3].takeIf { it != NULL_PLACEHOLDER }
                    val employeeImageUri = fields[4].takeIf { it != NULL_PLACEHOLDER }

                    // Handle legacy data (only 5 fields) by providing nulls for email/phone
                    RecentlyWorked(
                        id = 0,
                        employeeId = employeeId,
                        lastWorkedDate = lastWorkedDate,
                        employeeName = employeeName,
                        employeeRole = employeeRole,
                        employeeImageUri = employeeImageUri,
                        employeeEmail = null,
                        employeePhoneNumber = null
                    )
                } else {
                    null
                }
            }.toMutableList()
    }

    private fun saveRecentlyWorkedList(list: List<RecentlyWorked>) {
        val pref = getSharedPreferences(RECENTLY_WORKED_PREF, Context.MODE_PRIVATE)

        val fullString = list.joinToString(separator = ITEM_SEPARATOR) { recent ->
            val name = recent.employeeName ?: NULL_PLACEHOLDER
            val role = recent.employeeRole ?: NULL_PLACEHOLDER
            val uri = recent.employeeImageUri ?: NULL_PLACEHOLDER
            val email = recent.employeeEmail ?: NULL_PLACEHOLDER
            val phone = recent.employeePhoneNumber ?: NULL_PLACEHOLDER

            "${recent.employeeId}${FIELD_SEPARATOR}${recent.lastWorkedDate}${FIELD_SEPARATOR}${name}${FIELD_SEPARATOR}${role}${FIELD_SEPARATOR}${uri}${FIELD_SEPARATOR}${email}${FIELD_SEPARATOR}${phone}"
        }

        pref.edit().putString(RECENTLY_WORKED_KEY, fullString).apply()
    }

    private fun loadRecentlyWorked() {
        val recentList = getRecentlyWorkedList()
        recentlyWorkedAdapter.submitList(recentList)
    }

    private fun saveRecentEmployee(newRecent: RecentlyWorked) {
        val currentList = getRecentlyWorkedList()
        currentList.removeAll { it.employeeId == newRecent.employeeId }
        currentList.add(0, newRecent)
        val newList = currentList.take(MAX_RECENT_COUNT)
        saveRecentlyWorkedList(newList)
    }

    private fun logout() {
        val pref = getSharedPreferences("MyAppPref", MODE_PRIVATE)
        pref.edit().putBoolean("isLoggedIn", false).apply()
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}