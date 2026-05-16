package com.example.blogapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    private val format = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.ENGLISH)

    fun format(timestamp: Long): String = format.format(Date(timestamp))
}
