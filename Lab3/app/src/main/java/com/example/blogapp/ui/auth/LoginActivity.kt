package com.example.blogapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.blogapp.R
import com.example.blogapp.data.AppDatabase
import com.example.blogapp.data.repository.UserRepository
import com.example.blogapp.databinding.ActivityLoginBinding
import com.example.blogapp.session.SessionManager
import com.example.blogapp.ui.main.MainActivity
import com.example.blogapp.viewmodel.AuthViewModel
import com.example.blogapp.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    private val authViewModel: AuthViewModel by viewModels {
        val db = AppDatabase.getInstance(applicationContext)
        ViewModelFactory(userRepo = UserRepository(db.userDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        if (sessionManager.isLoggedIn()) {
            goToMain()
            return
        }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text?.toString().orEmpty()
            val password = binding.passwordInput.text?.toString().orEmpty()
            authViewModel.login(username, password)
        }

        binding.registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        authViewModel.loginResult.observe(this) { result ->
            result.onSuccess { user ->
                sessionManager.saveUserId(user.id)
                goToMain()
            }
            result.onFailure {
                Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
