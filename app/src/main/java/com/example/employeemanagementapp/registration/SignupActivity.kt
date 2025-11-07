package com.example.employeemanagementapp.registration

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.employeemanagementapp.databinding.ActivitySignupBinding
import com.example.employeemanagementapp.ui.EmployeeListActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.regBtn.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            if (!validateInput(name, email, password, confirmPassword)) return@setOnClickListener

            // Save to SharedPreferences
            val sharedPref = getSharedPreferences("MyAppPref", MODE_PRIVATE)
            sharedPref.edit().apply {
                putString("name", name)
                putString("email", email)
                putString("pass", password)
                apply()
            }

            Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, EmployeeListActivity::class.java)
            intent.putExtra("userName", name)
            startActivity(intent)
            finish()
        }

        binding.backWlc.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }

        binding.cLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateInput(name: String, email: String, password: String, confirmPassword: String): Boolean {
        when {
            name.isEmpty() -> {
                Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show()
                return false
            }
            email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Valid email required", Toast.LENGTH_SHORT).show()
                return false
            }
            password.length < 8 -> {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                return false
            }
            password != confirmPassword -> {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return false
            }
            else -> return true
        }
    }
}
