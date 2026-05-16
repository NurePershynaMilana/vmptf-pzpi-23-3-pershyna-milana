package com.example.blogapp.data.relations

import androidx.room.Embedded
import com.example.blogapp.data.entities.Post

data class PostWithDetails(
    @Embedded val post: Post,
    val categoryName: String,
    val authorUsername: String
)
