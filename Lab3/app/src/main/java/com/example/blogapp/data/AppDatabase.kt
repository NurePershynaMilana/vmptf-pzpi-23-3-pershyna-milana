package com.example.blogapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.blogapp.data.dao.CategoryDao
import com.example.blogapp.data.dao.CommentDao
import com.example.blogapp.data.dao.PostDao
import com.example.blogapp.data.dao.UserDao
import com.example.blogapp.data.entities.Category
import com.example.blogapp.data.entities.Comment
import com.example.blogapp.data.entities.Post
import com.example.blogapp.data.entities.User

@Database(
    entities = [User::class, Category::class, Post::class, Comment::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "blog_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
