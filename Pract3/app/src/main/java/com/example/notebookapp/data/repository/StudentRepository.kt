package com.example.notebookapp.data.repository

import com.example.notebookapp.data.dao.StudentDao
import com.example.notebookapp.data.entities.Student

class StudentRepository(private val dao: StudentDao) {
    fun getAll() = dao.getAll()
    suspend fun insert(student: Student): Long = dao.insert(student)
    suspend fun deleteById(id: Long) = dao.deleteById(id)
}
