package com.example.blogapp.data.repository

import androidx.lifecycle.LiveData
import com.example.blogapp.data.dao.PostDao
import com.example.blogapp.data.entities.Post
import com.example.blogapp.data.relations.PostWithDetails

class PostRepository(private val dao: PostDao) {
    suspend fun insert(post: Post): Long = dao.insert(post)
    suspend fun update(post: Post) = dao.update(post)
    suspend fun deleteById(id: Long) = dao.deleteById(id)
    suspend fun getById(id: Long): Post? = dao.getById(id)
    fun getAllWithDetails(): LiveData<List<PostWithDetails>> = dao.getAllWithDetails()
    fun search(query: String, categoryId: Long): LiveData<List<PostWithDetails>> =
        dao.searchWithDetails(query, categoryId)
    fun getByIdWithDetails(id: Long): LiveData<PostWithDetails?> = dao.getByIdWithDetails(id)
}
