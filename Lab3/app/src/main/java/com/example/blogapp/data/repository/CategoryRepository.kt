package com.example.blogapp.data.repository

import androidx.lifecycle.LiveData
import com.example.blogapp.data.dao.CategoryDao
import com.example.blogapp.data.entities.Category

class CategoryRepository(private val dao: CategoryDao) {
    suspend fun insert(category: Category): Long = dao.insert(category)
    suspend fun insertAll(categories: List<Category>) = dao.insertAll(categories)
    fun getAll(): LiveData<List<Category>> = dao.getAll()
    suspend fun getAllSync(): List<Category> = dao.getAllSync()
    suspend fun deleteById(id: Long) = dao.deleteById(id)
    suspend fun count(): Int = dao.count()
}
