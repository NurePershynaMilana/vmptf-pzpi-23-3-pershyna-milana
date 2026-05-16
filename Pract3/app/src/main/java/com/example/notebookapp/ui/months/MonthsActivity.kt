package com.example.notebookapp.ui.months

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notebookapp.R
import com.example.notebookapp.databinding.ActivityMonthsBinding

class MonthsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonthsBinding

    private val months by lazy { resources.getStringArray(R.array.months_array) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.showButton.setOnClickListener { showMonth() }
    }

    private fun showMonth() {
        val input = binding.monthInput.text?.toString()?.trim() ?: ""
        val number = input.toIntOrNull()

        if (number == null || number < 1 || number > 12) {
            binding.monthInputLayout.error = getString(R.string.months_error)
            binding.resultCard.visibility = android.view.View.GONE
            return
        }

        binding.monthInputLayout.error = null
        binding.monthNumber.text = number.toString()
        binding.monthName.text = months[number - 1]
        binding.resultCard.visibility = android.view.View.VISIBLE
    }
}
