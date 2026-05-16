package com.example.notebookapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notebookapp.data.entities.Attendance

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(attendance: Attendance)

    @Query("SELECT * FROM attendance WHERE lessonId = :lessonId")
    fun getForLesson(lessonId: Long): LiveData<List<Attendance>>
}
