package com.example.notebookapp.ui.notes

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.notebookapp.R
import com.example.notebookapp.databinding.DialogAddNoteBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddNoteDialog(
    private val onSave: (title: String, content: String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAddNoteBinding.inflate(LayoutInflater.from(requireContext()))

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.TransparentDialog)
            .setView(binding.root)
            .create()

        binding.cancelButton.setOnClickListener { dismiss() }
        binding.saveButton.setOnClickListener {
            val title = binding.titleInput.text?.toString()?.trim() ?: ""
            val content = binding.contentInput.text?.toString()?.trim() ?: ""
            if (title.isNotEmpty()) {
                onSave(title, content)
                dismiss()
            } else {
                binding.titleInputLayout.error = getString(R.string.note_title_error)
            }
        }

        return dialog
    }
}
