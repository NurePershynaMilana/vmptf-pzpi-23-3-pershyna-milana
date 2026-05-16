package com.example.blogapp.data.repository

import androidx.lifecycle.LiveData
import com.example.blogapp.data.dao.CommentDao
import com.example.blogapp.data.entities.Comment
import com.example.blogapp.data.relations.CommentWithAuthor

class CommentRepository(private val dao: CommentDao) {
    suspend fun insert(comment: Comment): Long = dao.insert(comment)
    fun getForPostWithAuthor(postId: Long): LiveData<List<CommentWithAuthor>> =
        dao.getForPostWithAuthor(postId)
}
