package com.example.blogapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blogapp.data.entities.Category
import com.example.blogapp.data.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel(private val categoryRepo: CategoryRepository) : ViewModel() {

    val categories: LiveData<List<Category>> = categoryRepo.getAll()

    fun addCategory(name: String) {
        viewModelScope.launch {
            categoryRepo.insert(Category(name = name.trim()))
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            categoryRepo.deleteById(id)
        }
    }
}
