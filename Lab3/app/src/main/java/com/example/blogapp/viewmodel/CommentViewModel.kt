package com.example.blogapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.data.entities.Comment
import com.example.blogapp.data.relations.CommentWithAuthor
import com.example.blogapp.data.repository.CommentRepository
import kotlinx.coroutines.launch

class CommentViewModel(private val commentRepo: CommentRepository) : ViewModel() {

    fun getCommentsForPost(postId: Long): LiveData<List<CommentWithAuthor>> =
        commentRepo.getForPostWithAuthor(postId)

    fun addComment(postId: Long, authorId: Long, text: String) {
        viewModelScope.launch {
            commentRepo.insert(
                Comment(
                    postId = postId,
                    authorId = authorId,
                    text = text,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }
}
