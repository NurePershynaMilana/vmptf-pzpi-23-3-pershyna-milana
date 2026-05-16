package com.example.blogapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.data.entities.User
import com.example.blogapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepo: UserRepository) : ViewModel() {

    val loginResult = MutableLiveData<Result<User>>()
    val registerResult = MutableLiveData<Result<User>>()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = userRepo.getByUsername(username.trim())
            if (user != null && user.password == password) {
                loginResult.postValue(Result.success(user))
            } else {
                loginResult.postValue(Result.failure(Exception("invalid_credentials")))
            }
        }
    }

    fun register(username: String, password: String, passwordConfirm: String) {
        viewModelScope.launch {
            val trimmedUsername = username.trim()
            when {
                trimmedUsername.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() -> {
                    registerResult.postValue(Result.failure(Exception("fields_empty")))
                }
                trimmedUsername.length < 3 -> {
                    registerResult.postValue(Result.failure(Exception("short_username")))
                }
                password != passwordConfirm -> {
                    registerResult.postValue(Result.failure(Exception("passwords_mismatch")))
                }
                userRepo.usernameExists(trimmedUsername) > 0 -> {
                    registerResult.postValue(Result.failure(Exception("username_exists")))
                }
                else -> {
                    val user = User(
                        username = trimmedUsername,
                        password = password,
                        createdAt = System.currentTimeMillis()
                    )
                    val id = userRepo.insert(user)
                    registerResult.postValue(Result.success(user.copy(id = id)))
                }
            }
        }
    }
}
