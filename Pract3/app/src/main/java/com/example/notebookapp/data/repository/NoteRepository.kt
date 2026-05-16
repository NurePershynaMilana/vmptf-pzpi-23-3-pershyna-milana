package com.example.notebookapp.data.repository

import com.example.notebookapp.data.dao.NoteDao
import com.example.notebookapp.data.entities.Note

class NoteRepository(private val dao: NoteDao) {
    fun getAll() = dao.getAll()
    suspend fun insert(note: Note): Long = dao.insert(note)
    suspend fun deleteById(id: Long) = dao.deleteById(id)
    suspend fun getById(id: Long): Note? = dao.getById(id)
}
