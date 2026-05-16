package com.example.blogapp

import android.app.Application
import com.example.blogapp.data.AppDatabase
import com.example.blogapp.data.SeedHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlogApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getInstance(this)
        CoroutineScope(Dispatchers.IO).launch {
            SeedHelper.seedIfEmpty(db)
        }
    }
}
