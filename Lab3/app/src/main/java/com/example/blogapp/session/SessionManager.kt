package com.example.blogapp.session

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("blog_session", Context.MODE_PRIVATE)

    fun saveUserId(id: Long) {
        prefs.edit().putLong("user_id", id).apply()
    }

    fun getUserId(): Long = prefs.getLong("user_id", -1L)

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean = getUserId() != -1L
}
