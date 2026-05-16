package com.example.notebookapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notebookapp.data.entities.Student
import com.example.notebookapp.data.repository.StudentRepository
import kotlinx.coroutines.launch

class StudentsViewModel(private val repository: StudentRepository) : ViewModel() {
    val students = repository.getAll()

    fun add(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.insert(Student(name = name.trim()))
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }
}
