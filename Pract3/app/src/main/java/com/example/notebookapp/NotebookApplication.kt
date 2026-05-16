package com.example.notebookapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class NotebookApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}
