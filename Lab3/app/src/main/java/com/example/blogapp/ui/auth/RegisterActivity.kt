package com.example.blogapp.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.blogapp.R
import com.example.blogapp.data.AppDatabase
import com.example.blogapp.data.repository.UserRepository
import com.example.blogapp.databinding.ActivityRegisterBinding
import com.example.blogapp.viewmodel.AuthViewModel
import com.example.blogapp.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val authViewModel: AuthViewModel by viewModels {
        val db = AppDatabase.getInstance(applicationContext)
        ViewModelFactory(userRepo = UserRepository(db.userDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginLink.setOnClickListener { finish() }

        binding.registerButton.setOnClickListener {
            binding.usernameLayout.error = null
            binding.passwordLayout.error = null
            binding.passwordConfirmLayout.error = null

            val username = binding.usernameInput.text?.toString().orEmpty()
            val password = binding.passwordInput.text?.toString().orEmpty()
            val confirm = binding.passwordConfirmInput.text?.toString().orEmpty()
            authViewModel.register(username, password, confirm)
        }

        authViewModel.registerResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                finish()
            }
            result.onFailure { error ->
                when (error.message) {
                    "fields_empty" -> Toast.makeText(this, getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show()
                    "short_username" -> binding.usernameLayout.error = getString(R.string.error_short_username)
                    "passwords_mismatch" -> binding.passwordConfirmLayout.error = getString(R.string.error_passwords_mismatch)
                    "username_exists" -> binding.usernameLayout.error = getString(R.string.error_username_exists)
                    else -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
