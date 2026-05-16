package com.example.blogapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.blogapp.data.entities.Comment
import com.example.blogapp.data.relations.CommentWithAuthor

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(comment: Comment): Long

    @Query(
        """
        SELECT comments.*, users.username AS authorUsername
        FROM comments
        INNER JOIN users ON comments.authorId = users.id
        WHERE comments.postId = :postId
        ORDER BY comments.createdAt ASC
        """
    )
    fun getForPostWithAuthor(postId: Long): LiveData<List<CommentWithAuthor>>
}
