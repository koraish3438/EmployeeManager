package com.example.employeemanagementapp.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.employeemanagementapp.R
import com.example.employeemanagementapp.databinding.ActivityEmployeeDetailsBinding
import com.example.employeemanagementapp.data.EmployeeStats

class EmployeeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeDetailsBinding

    companion object {
        const val EXTRA_EMPLOYEE_NAME = "extra_employee_name"
        const val EXTRA_EMPLOYEE_ROLE = "extra_employee_role"
        const val EXTRA_EMPLOYEE_EMAIL = "extra_employee_email"
        const val EXTRA_EMPLOYEE_PHONE = "extra_employee_phone"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmployeeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val employeeName = intent.getStringExtra(EXTRA_EMPLOYEE_NAME) ?: "Name Not Available"
        val employeeRole = intent.getStringExtra(EXTRA_EMPLOYEE_ROLE) ?: "Role Not Available"
        val employeeEmail = intent.getStringExtra(EXTRA_EMPLOYEE_EMAIL) ?: "N/A"
        val employeePhone = intent.getStringExtra(EXTRA_EMPLOYEE_PHONE) ?: "N/A"

        val employeeData = EmployeeStats(
            employeeId = "EMP001",
            name = employeeName,
            role = employeeRole,
            email = employeeEmail,
            phoneNumber = employeePhone,
            likes = 45,
            thanks = 12,
            credits = 5
        )

        displayEmployeeDetails(employeeData)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun displayEmployeeDetails(employee: EmployeeStats) {
        binding.tvNameDetails.text = employee.name
        binding.tvRoleDetails.text = employee.role

        binding.cardContact.findViewById<TextView>(R.id.tvEmployeeEmail)?.text = employee.email
        binding.cardContact.findViewById<TextView>(R.id.tvEmployeePhone)?.text = employee.phoneNumber

        setupStats(employee.likes, employee.thanks, employee.credits)
    }

    private fun setupStats(likes: Int, thanks: Int, credits: Int) {

        val likesStat = binding.statLikes.root
        likesStat.findViewById<ImageView>(R.id.imgStatIcon).setImageResource(R.drawable.outline_thumb_up_24)
        likesStat.findViewById<TextView>(R.id.tvStatValue).text = likes.toString()
        likesStat.findViewById<TextView>(R.id.tvStatLabel).text = "Likes"
        likesStat.findViewById<ImageView>(R.id.imgStatIcon).setColorFilter(getColor(R.color.green_400))

        val thanksStat = binding.statThanks.root
        thanksStat.findViewById<ImageView>(R.id.imgStatIcon).setImageResource(R.drawable.handshake)
        thanksStat.findViewById<TextView>(R.id.tvStatValue).text = thanks.toString()
        thanksStat.findViewById<TextView>(R.id.tvStatLabel).text = "Thanks"
        thanksStat.findViewById<ImageView>(R.id.imgStatIcon).setColorFilter(getColor(R.color.blue_400))

        val creditsStat = binding.statCredits.root
        creditsStat.findViewById<ImageView>(R.id.imgStatIcon).setImageResource(R.drawable.star)
        creditsStat.findViewById<TextView>(R.id.tvStatValue).text = credits.toString()
        creditsStat.findViewById<TextView>(R.id.tvStatLabel).text = "Credits"
        creditsStat.findViewById<ImageView>(R.id.imgStatIcon).setColorFilter(getColor(R.color.orange_400))
    }
}