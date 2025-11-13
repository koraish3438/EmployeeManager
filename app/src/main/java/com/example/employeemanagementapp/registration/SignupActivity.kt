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

            val sharedPref = getSharedPreferences("MyAppPref", MODE_PRIVATE)

            // Checking if the user already exists
            val users = sharedPref.getStringSet("userEmails", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

            if (users.contains(email)) {
                Toast.makeText(this, "Account with this email already exists. Please Login.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Saving new user data using email as part of the key
            sharedPref.edit().apply {
                putString("user_${email}_name", name)
                putString("user_${email}_pass", password)
                users.add(email)
                putStringSet("userEmails", users)

                // Automatically logging in after successful sign-up
                putString("currentLoggedInEmail", email) // Saving the email of the currently logged-in user
                putBoolean("isLoggedIn", true)
                apply()
            }

            Toast.makeText(this, "Sign Up Successful! Logged in as $name", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, EmployeeListActivity::class.java)
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
        return when {
            name.isEmpty() -> {
                Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show()
                false
            }
            email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Valid email required", Toast.LENGTH_SHORT).show()
                false
            }
            password.length < 8 -> {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                false
            }
            password != confirmPassword -> {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }
}