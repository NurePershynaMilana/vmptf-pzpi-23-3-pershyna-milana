package com.example.blogapp.data

import com.example.blogapp.data.entities.Category
import com.example.blogapp.data.entities.User

object SeedHelper {
    suspend fun seedIfEmpty(db: AppDatabase) {
        val userDao = db.userDao()
        val categoryDao = db.categoryDao()

        if (userDao.usernameExists("admin") == 0) {
            userDao.insert(
                User(
                    username = "admin",
                    password = "admin",
                    createdAt = System.currentTimeMillis()
                )
            )
        }

        if (categoryDao.count() == 0) {
            categoryDao.insertAll(
                listOf(
                    Category(name = "Technology"),
                    Category(name = "Travel"),
                    Category(name = "Food"),
                    Category(name = "Sports"),
                    Category(name = "Other")
                )
            )
        }
    }
}
