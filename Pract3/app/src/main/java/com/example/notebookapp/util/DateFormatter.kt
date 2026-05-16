package com.example.notebookapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    private val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    fun format(timestamp: Long): String = format.format(Date(timestamp))
}
