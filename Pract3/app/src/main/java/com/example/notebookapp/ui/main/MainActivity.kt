package com.example.notebookapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notebookapp.databinding.ActivityMainBinding
import com.example.notebookapp.ui.journal.JournalActivity
import com.example.notebookapp.ui.months.MonthsActivity
import com.example.notebookapp.ui.notes.NotesActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardMonths.setOnClickListener {
            startActivity(Intent(this, MonthsActivity::class.java))
        }
        binding.cardNotes.setOnClickListener {
            startActivity(Intent(this, NotesActivity::class.java))
        }
        binding.cardJournal.setOnClickListener {
            startActivity(Intent(this, JournalActivity::class.java))
        }
    }
}
