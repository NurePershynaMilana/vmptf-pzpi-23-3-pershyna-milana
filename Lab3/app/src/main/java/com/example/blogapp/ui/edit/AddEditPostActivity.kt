package com.example.blogapp.ui.edit

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.blogapp.R
import com.example.blogapp.data.AppDatabase
import com.example.blogapp.data.entities.Category
import com.example.blogapp.data.repository.CategoryRepository
import com.example.blogapp.data.repository.PostRepository
import com.example.blogapp.databinding.ActivityAddEditPostBinding
import com.example.blogapp.session.SessionManager
import com.example.blogapp.viewmodel.CategoryViewModel
import com.example.blogapp.viewmodel.PostViewModel
import com.example.blogapp.viewmodel.ViewModelFactory

class AddEditPostActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_POST_ID = "post_id"
    }

    private lateinit var binding: ActivityAddEditPostBinding
    private lateinit var sessionManager: SessionManager
    private var categoryList: List<Category> = emptyList()
    private var postId: Long = -1L
    private var existingCategoryId: Long = -1L

    private val postViewModel: PostViewModel by viewModels {
        val db = AppDatabase.getInstance(applicationContext)
        ViewModelFactory(postRepo = PostRepository(db.postDao()))
    }

    private val categoryViewModel: CategoryViewModel by viewModels {
        val db = AppDatabase.getInstance(applicationContext)
        ViewModelFactory(categoryRepo = CategoryRepository(db.categoryDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        postId = intent.getLongExtra(EXTRA_POST_ID, -1L)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (postId != -1L) getString(R.string.edit_post) else getString(R.string.new_post)

        if (postId != -1L) {
            postViewModel.getPostById(postId).observe(this) { postWithDetails ->
                postWithDetails ?: return@observe
                binding.titleEdit.setText(postWithDetails.post.title)
                binding.contentEdit.setText(postWithDetails.post.content)
                existingCategoryId = postWithDetails.post.categoryId
                selectCategoryInSpinner()
            }
        }

        categoryViewModel.categories.observe(this) { categories ->
            categoryList = categories
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                categories.map { it.name }
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = adapter
            selectCategoryInSpinner()
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEdit.text?.toString().orEmpty().trim()
            val content = binding.contentEdit.text?.toString().orEmpty().trim()

            binding.titleLayout.error = null
            binding.contentLayout.error = null

            if (title.isEmpty()) {
                binding.titleLayout.error = getString(R.string.error_title_empty)
                return@setOnClickListener
            }
            if (content.isEmpty()) {
                binding.contentLayout.error = getString(R.string.error_content_empty)
                return@setOnClickListener
            }
            if (categoryList.isEmpty()) return@setOnClickListener

            val selectedIndex = binding.categorySpinner.selectedItemPosition
            val categoryId = if (selectedIndex >= 0 && selectedIndex < categoryList.size) {
                categoryList[selectedIndex].id
            } else {
                categoryList.first().id
            }

            if (postId == -1L) {
                postViewModel.addPost(title, content, categoryId, sessionManager.getUserId())
            } else {
                postViewModel.updatePost(postId, title, content, categoryId)
            }
            finish()
        }
    }

    private fun selectCategoryInSpinner() {
        if (existingCategoryId == -1L || categoryList.isEmpty()) return
        val idx = categoryList.indexOfFirst { it.id == existingCategoryId }
        if (idx >= 0) binding.categorySpinner.setSelection(idx)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
