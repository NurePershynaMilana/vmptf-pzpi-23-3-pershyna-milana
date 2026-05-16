package com.example.blogapp.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blogapp.data.AppDatabase
import com.example.blogapp.data.repository.CommentRepository
import com.example.blogapp.data.repository.PostRepository
import com.example.blogapp.databinding.ActivityPostDetailBinding
import com.example.blogapp.session.SessionManager
import com.example.blogapp.util.DateFormatter
import com.example.blogapp.viewmodel.CommentViewModel
import com.example.blogapp.viewmodel.PostViewModel
import com.example.blogapp.viewmodel.ViewModelFactory

class PostDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_POST_ID = "post_id"
    }

    private lateinit var binding: ActivityPostDetailBinding
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var sessionManager: SessionManager

    private val postViewModel: PostViewModel by viewModels {
        val db = AppDatabase.getInstance(applicationContext)
        ViewModelFactory(postRepo = PostRepository(db.postDao()))
    }

    private val commentViewModel: CommentViewModel by viewModels {
        val db = AppDatabase.getInstance(applicationContext)
        ViewModelFactory(commentRepo = CommentRepository(db.commentDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sessionManager = SessionManager(this)

        val postId = intent.getLongExtra(EXTRA_POST_ID, -1L)
        if (postId == -1L) {
            finish()
            return
        }

        commentAdapter = CommentAdapter()
        binding.commentsRecycler.layoutManager = LinearLayoutManager(this)
        binding.commentsRecycler.adapter = commentAdapter

        postViewModel.getPostById(postId).observe(this) { postWithDetails ->
            if (postWithDetails == null) return@observe
            supportActionBar?.title = postWithDetails.post.title
            binding.postTitle.text = postWithDetails.post.title
            binding.postMeta.text = "${postWithDetails.authorUsername} • ${postWithDetails.categoryName} • ${DateFormatter.format(postWithDetails.post.createdAt)}"
            binding.postContent.text = postWithDetails.post.content
        }

        commentViewModel.getCommentsForPost(postId).observe(this) { comments ->
            commentAdapter.submitList(comments)
            binding.commentsEmpty.visibility = if (comments.isEmpty()) View.VISIBLE else View.GONE
            binding.commentsRecycler.visibility = if (comments.isEmpty()) View.GONE else View.VISIBLE
        }

        binding.sendButton.setOnClickListener {
            val text = binding.commentInput.text?.toString().orEmpty().trim()
            if (text.isNotEmpty()) {
                val authorId = sessionManager.getUserId()
                commentViewModel.addComment(postId, authorId, text)
                binding.commentInput.setText("")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
