package com.example.notebookapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notebookapp.data.entities.Student

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: Student): Long

    @Query("DELETE FROM students WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM students ORDER BY name ASC")
    fun getAll(): LiveData<List<Student>>
}
