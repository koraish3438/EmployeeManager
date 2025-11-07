package com.example.employeemanagementapp.registration

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.employeemanagementapp.R
import com.example.employeemanagementapp.databinding.ActivityWelcomeBinding
import com.example.employeemanagementapp.ui.EmployeeListActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // ViewBinding setup
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lottie Animation setup
        binding.welcomeAnim.apply {
            setAnimation(R.raw.businessman)
            loop(true)
            playAnimation()
        }

        // Sign Up Button click
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // Login Button click
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Skip TextView click
        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, EmployeeListActivity::class.java)
            intent.putExtra("userName", "Guest")
            startActivity(intent)
            finish()
        }
    }
}