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
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check login state
        val sharedPref = getSharedPreferences("MyAppPref", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            startActivity(Intent(this, EmployeeListActivity::class.java))
            finish()
            return
        }

        // Lottie Animation
        binding.welcomeAnim.setAnimation(R.raw.businessman)
        binding.welcomeAnim.playAnimation()

        // Sign Up Button
        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // Login Button
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Guest Continue
        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, EmployeeListActivity::class.java)
            intent.putExtra("userName", "Guest")
            startActivity(intent)
            finish()
        }
    }
}
