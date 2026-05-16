package com.example.notebookapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class Lesson(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val date: Long
)
