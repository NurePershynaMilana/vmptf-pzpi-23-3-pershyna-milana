package com.example.blogapp.ui.categories

import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blogapp.R
import com.example.blogapp.data.AppDatabase
import com.example.blogapp.data.repository.CategoryRepository
import com.example.blogapp.databinding.ActivityCategoriesBinding
import com.example.blogapp.viewmodel.CategoryViewModel
import com.example.blogapp.viewmodel.ViewModelFactory

class CategoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var categoryAdapter: CategoryAdapter

    private val categoryViewModel: CategoryViewModel by viewModels {
        val db = AppDatabase.getInstance(applicationContext)
        ViewModelFactory(categoryRepo = CategoryRepository(db.categoryDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.categories)

        categoryAdapter = CategoryAdapter { category ->
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.confirm_delete_category))
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    categoryViewModel.deleteCategory(category.id)
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }

        binding.categoriesRecycler.layoutManager = LinearLayoutManager(this)
        binding.categoriesRecycler.adapter = categoryAdapter

        categoryViewModel.categories.observe(this) { categories ->
            categoryAdapter.submitList(categories)
        }

        binding.addCategoryFab.setOnClickListener {
            val input = EditText(this)
            input.hint = getString(R.string.category_name_hint)
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.add_category_title))
                .setView(input)
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    val name = input.text?.toString().orEmpty().trim()
                    if (name.isNotEmpty()) {
                        categoryViewModel.addCategory(name)
                    }
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
