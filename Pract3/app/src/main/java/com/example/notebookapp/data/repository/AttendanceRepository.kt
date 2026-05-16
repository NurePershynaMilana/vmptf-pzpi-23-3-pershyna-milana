package com.example.notebookapp.data.repository

import com.example.notebookapp.data.dao.AttendanceDao
import com.example.notebookapp.data.entities.Attendance

class AttendanceRepository(private val dao: AttendanceDao) {
    fun getForLesson(lessonId: Long) = dao.getForLesson(lessonId)
    suspend fun upsert(attendance: Attendance) = dao.upsert(attendance)
}
