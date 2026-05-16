package com.example.notebookapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.notebookapp.data.entities.Attendance
import com.example.notebookapp.data.entities.Student
import com.example.notebookapp.data.repository.AttendanceRepository
import com.example.notebookapp.data.repository.LessonRepository
import com.example.notebookapp.data.repository.StudentRepository
import kotlinx.coroutines.launch

data class StudentWithAttendance(val student: Student, val isPresent: Boolean)

class AttendanceViewModel(
    private val attendanceRepository: AttendanceRepository,
    private val lessonRepository: LessonRepository,
    private val studentRepository: StudentRepository,
) : ViewModel() {

    val lessons = lessonRepository.getAll()

    private val _selectedLessonId = MutableLiveData<Long?>(null)

    private val attendanceForLesson: LiveData<List<Attendance>> =
        _selectedLessonId.switchMap { id ->
            id?.let { attendanceRepository.getForLesson(it) } ?: MutableLiveData(emptyList())
        }

    val studentsWithAttendance: MediatorLiveData<List<StudentWithAttendance>> =
        MediatorLiveData<List<StudentWithAttendance>>().apply {
            var students: List<Student> = emptyList()
            var attendance: List<Attendance> = emptyList()

            addSource(studentRepository.getAll()) { s ->
                students = s
                value = combine(students, attendance)
            }
            addSource(attendanceForLesson) { a ->
                attendance = a
                value = combine(students, attendance)
            }
        }

    fun selectLesson(lessonId: Long) {
        _selectedLessonId.value = lessonId
    }

    fun setAttendance(studentId: Long, lessonId: Long, isPresent: Boolean) {
        viewModelScope.launch {
            attendanceRepository.upsert(
                Attendance(studentId = studentId, lessonId = lessonId, isPresent = isPresent)
            )
        }
    }

    private fun combine(students: List<Student>, attendance: List<Attendance>) =
        students.map { s ->
            StudentWithAttendance(s, attendance.find { it.studentId == s.id }?.isPresent ?: false)
        }
}
