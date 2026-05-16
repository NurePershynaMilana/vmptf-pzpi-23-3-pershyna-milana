package com.example.blogapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.blogapp.data.entities.Post
import com.example.blogapp.data.relations.PostWithDetails

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(post: Post): Long

    @Update
    suspend fun update(post: Post)

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Post?

    @Query(
        """
        SELECT posts.*, categories.name AS categoryName, users.username AS authorUsername
        FROM posts
        LEFT JOIN categories ON posts.categoryId = categories.id
        INNER JOIN users ON posts.authorId = users.id
        ORDER BY posts.createdAt DESC
        """
    )
    fun getAllWithDetails(): LiveData<List<PostWithDetails>>

    @Query(
        """
        SELECT posts.*, categories.name AS categoryName, users.username AS authorUsername
        FROM posts
        LEFT JOIN categories ON posts.categoryId = categories.id
        INNER JOIN users ON posts.authorId = users.id
        WHERE (posts.title LIKE '%' || :query || '%' OR posts.content LIKE '%' || :query || '%')
        AND (:categoryId = -1 OR posts.categoryId = :categoryId)
        ORDER BY posts.createdAt DESC
        """
    )
    fun searchWithDetails(query: String, categoryId: Long): LiveData<List<PostWithDetails>>

    @Query(
        """
        SELECT posts.*, categories.name AS categoryName, users.username AS authorUsername
        FROM posts
        LEFT JOIN categories ON posts.categoryId = categories.id
        INNER JOIN users ON posts.authorId = users.id
        WHERE posts.id = :id
        LIMIT 1
        """
    )
    fun getByIdWithDetails(id: Long): LiveData<PostWithDetails?>
}
