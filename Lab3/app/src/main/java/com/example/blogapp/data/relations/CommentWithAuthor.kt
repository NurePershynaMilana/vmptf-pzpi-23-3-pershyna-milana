package com.example.blogapp.data.relations

import androidx.room.Embedded
import com.example.blogapp.data.entities.Comment

data class CommentWithAuthor(
    @Embedded val comment: Comment,
    val authorUsername: String
)
