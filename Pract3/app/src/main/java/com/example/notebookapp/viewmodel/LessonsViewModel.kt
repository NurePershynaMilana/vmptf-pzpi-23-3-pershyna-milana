package com.example.notebookapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notebookapp.data.entities.Lesson
import com.example.notebookapp.data.repository.LessonRepository
import kotlinx.coroutines.launch

class LessonsViewModel(private val repository: LessonRepository) : ViewModel() {
    val lessons = repository.getAll()

    fun add(name: String, date: Long) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.insert(Lesson(name = name.trim(), date = date))
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }
}
