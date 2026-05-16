package com.example.notebookapp.data.repository

import com.example.notebookapp.data.dao.LessonDao
import com.example.notebookapp.data.entities.Lesson

class LessonRepository(private val dao: LessonDao) {
    fun getAll() = dao.getAll()
    suspend fun insert(lesson: Lesson): Long = dao.insert(lesson)
    suspend fun deleteById(id: Long) = dao.deleteById(id)
}
