package com.example.notebookapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notebookapp.data.entities.Lesson

@Dao
interface LessonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lesson: Lesson): Long

    @Query("DELETE FROM lessons WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM lessons ORDER BY date DESC")
    fun getAll(): LiveData<List<Lesson>>
}
