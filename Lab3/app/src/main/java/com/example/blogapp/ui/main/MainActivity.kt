package com.example.blogapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.example.blogapp.R
import com.example.blogapp.data.AppDatabase
import com.example.blogapp.data.entities.Category
import com.example.blogapp.data.repository.CategoryRepository
import com.example.blogapp.data.repository.PostRepository
import com.example.blogapp.databinding.ActivityMainBinding
import com.example.blogapp.session.SessionManager
import com.example.blogapp.ui.auth.LoginActivity
import com.example.blogapp.ui.categories.CategoriesActivity
import com.example.blogapp.ui.detail.PostDetailActivity
import com.example.blogapp.ui.edit.AddEditPostActivity
import com.example.blogapp.viewmodel.CategoryViewModel
import com.example.blogapp.viewmodel.PostViewModel
import com.example.blogapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var postAdapter: PostAdapter

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        if (!sessionManager.isLoggedIn()) {
            goToLogin()
            return
        }

        setSupportActionBar(binding.toolbar)

        postAdapter = PostAdapter(
            onClick = { post ->
                val intent = Intent(this, PostDetailActivity::class.java)
                intent.putExtra(PostDetailActivity.EXTRA_POST_ID, post.post.id)
                startActivity(intent)
            },
            onLongClick = { post ->
                val currentUserId = sessionManager.getUserId()
                if (post.post.authorId == currentUserId) {
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.action_label))
                        .setItems(arrayOf(getString(R.string.edit), getString(R.string.delete))) { _, which ->
                            when (which) {
                                0 -> {
                                    val intent = Intent(this, AddEditPostActivity::class.java)
                                    intent.putExtra(AddEditPostActivity.EXTRA_POST_ID, post.post.id)
                                    startActivity(intent)
                                }
                                1 -> {
                                    AlertDialog.Builder(this)
                                        .setMessage(getString(R.string.confirm_delete_post))
                                        .setPositiveButton(getString(R.string.ok)) { _, _ ->
                                            postViewModel.deletePost(post.post.id)
                                        }
                                        .setNegativeButton(getString(R.string.cancel), null)
                                        .show()
                                }
                            }
                        }
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show()
                }
            }
        )

        binding.postsRecycler.layoutManager = LinearLayoutManager(this)
        binding.postsRecycler.adapter = postAdapter

        postViewModel.posts.observe(this) { posts ->
            postAdapter.submitList(posts)
            binding.emptyView.visibility = if (posts.isEmpty()) View.VISIBLE else View.GONE
            binding.postsRecycler.visibility = if (posts.isEmpty()) View.GONE else View.VISIBLE
        }

        categoryViewModel.categories.observe(this) { categories ->
            rebuildChips(categories)
        }

        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                postViewModel.setSearchQuery(newText.orEmpty())
                return true
            }
        })

        binding.addPostFab.setOnClickListener {
            startActivity(Intent(this, AddEditPostActivity::class.java))
        }
    }

    private fun rebuildChips(categories: List<Category>) {
        binding.categoryChips.removeAllViews()

        val allChip = Chip(this).apply {
            text = getString(R.string.all_categories)
            isCheckable = true
            isChecked = true
            tag = -1L
        }
        allChip.setOnClickListener { postViewModel.setCategory(-1L) }
        binding.categoryChips.addView(allChip)

        categories.forEach { cat ->
            val chip = Chip(this).apply {
                text = cat.name
                isCheckable = true
                tag = cat.id
            }
            chip.setOnClickListener { postViewModel.setCategory(cat.id) }
            binding.categoryChips.addView(chip)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_categories -> {
                startActivity(Intent(this, CategoriesActivity::class.java))
                true
            }
            R.id.action_logout -> {
                sessionManager.clear()
                goToLogin()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
