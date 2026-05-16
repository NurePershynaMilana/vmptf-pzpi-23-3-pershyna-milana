package com.example.blogapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.blogapp.data.entities.Post
import com.example.blogapp.data.relations.PostWithDetails
import com.example.blogapp.data.repository.PostRepository
import kotlinx.coroutines.launch

class PostViewModel(private val postRepo: PostRepository) : ViewModel() {

    private val _filter = MutableLiveData(Pair("", -1L))

    val posts: LiveData<List<PostWithDetails>> = _filter.switchMap { filter ->
        postRepo.search(filter.first, filter.second)
    }

    fun setSearchQuery(query: String) {
        val current = _filter.value ?: Pair("", -1L)
        _filter.value = current.copy(first = query)
    }

    fun setCategory(id: Long) {
        val current = _filter.value ?: Pair("", -1L)
        _filter.value = current.copy(second = id)
    }

    fun addPost(title: String, content: String, categoryId: Long, authorId: Long) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            postRepo.insert(
                Post(
                    title = title,
                    content = content,
                    categoryId = categoryId,
                    authorId = authorId,
                    createdAt = now,
                    updatedAt = now
                )
            )
        }
    }

    fun updatePost(id: Long, title: String, content: String, categoryId: Long) {
        viewModelScope.launch {
            val existing = postRepo.getById(id) ?: return@launch
            postRepo.update(
                existing.copy(
                    title = title,
                    content = content,
                    categoryId = categoryId,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun deletePost(id: Long) {
        viewModelScope.launch {
            postRepo.deleteById(id)
        }
    }

    fun getPostById(id: Long): LiveData<PostWithDetails?> = postRepo.getByIdWithDetails(id)
}
