package com.example.blogapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blogapp.data.repository.CategoryRepository
import com.example.blogapp.data.repository.CommentRepository
import com.example.blogapp.data.repository.PostRepository
import com.example.blogapp.data.repository.UserRepository

class ViewModelFactory(
    private val userRepo: UserRepository? = null,
    private val postRepo: PostRepository? = null,
    private val commentRepo: CommentRepository? = null,
    private val categoryRepo: CategoryRepository? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) ->
                AuthViewModel(userRepo!!) as T
            modelClass.isAssignableFrom(PostViewModel::class.java) ->
                PostViewModel(postRepo!!) as T
            modelClass.isAssignableFrom(CommentViewModel::class.java) ->
                CommentViewModel(commentRepo!!) as T
            modelClass.isAssignableFrom(CategoryViewModel::class.java) ->
                CategoryViewModel(categoryRepo!!) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
