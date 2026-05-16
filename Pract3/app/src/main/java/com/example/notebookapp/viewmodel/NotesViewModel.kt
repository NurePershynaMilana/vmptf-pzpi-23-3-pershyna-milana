package com.example.notebookapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notebookapp.data.entities.Note
import com.example.notebookapp.data.repository.NoteRepository
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NoteRepository) : ViewModel() {
    val notes = repository.getAll()

    fun insert(title: String, content: String) {
        viewModelScope.launch {
            repository.insert(Note(title = title, content = content))
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    suspend fun getById(id: Long) = repository.getById(id)
}
