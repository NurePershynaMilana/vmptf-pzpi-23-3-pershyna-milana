package com.example.notebookapp.ui.notes

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notebookapp.R
import com.example.notebookapp.databinding.ActivityNotesBinding
import com.example.notebookapp.viewmodel.NotesViewModel
import com.example.notebookapp.viewmodel.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding
    private val viewModel: NotesViewModel by viewModels { ViewModelFactory(application) }
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        adapter = NotesAdapter(
            onItemClick = { note ->
                startActivity(
                    Intent(this, NoteDetailActivity::class.java)
                        .putExtra(NoteDetailActivity.EXTRA_NOTE_ID, note.id)
                )
            },
            onDeleteClick = { note ->
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.note_delete_title)
                    .setMessage(R.string.note_delete_message)
                    .setPositiveButton(R.string.action_delete) { _, _ -> viewModel.delete(note.id) }
                    .setNegativeButton(R.string.action_cancel, null)
                    .show()
            },
        )

        binding.notesRecycler.layoutManager = LinearLayoutManager(this)
        binding.notesRecycler.adapter = adapter

        viewModel.notes.observe(this) { notes ->
            adapter.submitList(notes)
            binding.emptyView.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.addFab.setOnClickListener {
            AddNoteDialog { title, content -> viewModel.insert(title, content) }
                .show(supportFragmentManager, "add_note")
        }
    }
}
