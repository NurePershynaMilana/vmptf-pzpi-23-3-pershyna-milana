package com.example.notebookapp.ui.notes

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.notebookapp.databinding.ActivityNoteDetailBinding
import com.example.notebookapp.util.DateFormatter
import com.example.notebookapp.viewmodel.NotesViewModel
import com.example.notebookapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailBinding
    private val viewModel: NotesViewModel by viewModels { ViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1L)
        if (noteId == -1L) { finish(); return }

        lifecycleScope.launch {
            viewModel.getById(noteId)?.let { note ->
                binding.noteTitle.text = note.title
                binding.noteDate.text = DateFormatter.format(note.createdAt)
                binding.noteContent.text = note.content
            } ?: finish()
        }
    }

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }
}
