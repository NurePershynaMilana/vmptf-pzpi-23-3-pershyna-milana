package com.example.notebookapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notebookapp.data.AppDatabase
import com.example.notebookapp.data.repository.AttendanceRepository
import com.example.notebookapp.data.repository.LessonRepository
import com.example.notebookapp.data.repository.NoteRepository
import com.example.notebookapp.data.repository.StudentRepository

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getDatabase(application)

        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(NotesViewModel::class.java) ->
                NotesViewModel(NoteRepository(db.noteDao())) as T

            modelClass.isAssignableFrom(StudentsViewModel::class.java) ->
                StudentsViewModel(StudentRepository(db.studentDao())) as T

            modelClass.isAssignableFrom(LessonsViewModel::class.java) ->
                LessonsViewModel(LessonRepository(db.lessonDao())) as T

            modelClass.isAssignableFrom(AttendanceViewModel::class.java) ->
                AttendanceViewModel(
                    AttendanceRepository(db.attendanceDao()),
                    LessonRepository(db.lessonDao()),
                    StudentRepository(db.studentDao())
                ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
