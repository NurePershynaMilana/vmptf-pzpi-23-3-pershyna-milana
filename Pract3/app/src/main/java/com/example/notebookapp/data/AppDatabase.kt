package com.example.notebookapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notebookapp.data.dao.AttendanceDao
import com.example.notebookapp.data.dao.LessonDao
import com.example.notebookapp.data.dao.NoteDao
import com.example.notebookapp.data.dao.StudentDao
import com.example.notebookapp.data.entities.Attendance
import com.example.notebookapp.data.entities.Lesson
import com.example.notebookapp.data.entities.Note
import com.example.notebookapp.data.entities.Student

@Database(
    entities = [Note::class, Student::class, Lesson::class, Attendance::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun studentDao(): StudentDao
    abstract fun lessonDao(): LessonDao
    abstract fun attendanceDao(): AttendanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notebook_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
