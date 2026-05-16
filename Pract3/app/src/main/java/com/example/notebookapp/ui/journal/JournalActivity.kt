package com.example.notebookapp.ui.journal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notebookapp.R
import com.example.notebookapp.databinding.ActivityJournalBinding
import com.google.android.material.tabs.TabLayoutMediator

class JournalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJournalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.viewPager.adapter = JournalPagerAdapter(this)

        val tabs = arrayOf(R.string.tab_students, R.string.tab_lessons, R.string.tab_attendance)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.setText(tabs[pos])
        }.attach()
    }
}
