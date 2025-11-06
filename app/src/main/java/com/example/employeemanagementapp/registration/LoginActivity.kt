package com.example.employeemanagementapp.registration

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.employeemanagementapp.R
import com.example.employeemanagementapp.databinding.ActivityLoginBinding
import com.example.employeemanagementapp.ui.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLoginNow.setOnClickListener {
            val email = binding.etLoginEmail.text.toString().trim()
            val password = binding.etLoginPassword.text.toString().trim()

            if (!validateInput(email, password)) return@setOnClickListener

            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.backLogin.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }


        binding.goToRegister.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }


    private fun validateInput(email: String, password: String): Boolean {
        when {
            email.isEmpty() -> {
                Toast.makeText(this, "Email required", Toast.LENGTH_SHORT).show()
                return false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
                return false
            }
            password.isEmpty() -> {
                Toast.makeText(this, "Password required", Toast.LENGTH_SHORT).show()
                return false
            }
            password.length < 8 -> {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                return false
            }
            else -> return true
        }
    }
}