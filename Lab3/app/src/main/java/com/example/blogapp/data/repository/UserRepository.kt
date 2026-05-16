package com.example.blogapp.data.repository

import com.example.blogapp.data.dao.UserDao
import com.example.blogapp.data.entities.User

class UserRepository(private val dao: UserDao) {
    suspend fun insert(user: User): Long = dao.insert(user)
    suspend fun getByUsername(username: String): User? = dao.getByUsername(username)
    suspend fun getById(id: Long): User? = dao.getById(id)
    suspend fun usernameExists(username: String): Int = dao.usernameExists(username)
}
