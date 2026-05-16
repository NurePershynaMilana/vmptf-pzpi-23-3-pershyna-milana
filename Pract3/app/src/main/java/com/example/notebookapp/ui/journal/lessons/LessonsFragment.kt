package com.example.notebookapp.ui.journal.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notebookapp.R
import com.example.notebookapp.databinding.DialogAddLessonBinding
import com.example.notebookapp.databinding.FragmentLessonsBinding
import com.example.notebookapp.util.DateFormatter
import com.example.notebookapp.viewmodel.LessonsViewModel
import com.example.notebookapp.viewmodel.ViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LessonsFragment : Fragment() {

    private var _binding: FragmentLessonsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LessonsViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application)
    }
    private lateinit var adapter: LessonsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LessonsAdapter { lesson ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.lesson_delete_title)
                .setMessage(R.string.lesson_delete_message)
                .setPositiveButton(R.string.action_delete) { _, _ -> viewModel.delete(lesson.id) }
                .setNegativeButton(R.string.action_cancel, null)
                .show()
        }

        binding.lessonsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.lessonsRecycler.adapter = adapter

        viewModel.lessons.observe(viewLifecycleOwner) { lessons ->
            adapter.submitList(lessons)
            binding.emptyView.visibility = if (lessons.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.addLessonFab.setOnClickListener { showAddDialog() }
    }

    private fun showAddDialog() {
        var selectedDate: Long? = null
        val dialogBinding = DialogAddLessonBinding.inflate(LayoutInflater.from(requireContext()))

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.TransparentDialog)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.pickDateButton.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.lesson_pick_date))
                .setSelection(selectedDate ?: System.currentTimeMillis())
                .build()

            picker.addOnPositiveButtonClickListener { ms ->
                selectedDate = ms
                dialogBinding.selectedDateText.text = DateFormatter.format(ms)
            }

            picker.show(childFragmentManager, "date_picker")
        }

        dialogBinding.cancelButton.setOnClickListener { dialog.dismiss() }
        dialogBinding.saveButton.setOnClickListener {
            val name = dialogBinding.nameInput.text?.toString()?.trim() ?: ""
            when {
                name.isEmpty() -> dialogBinding.nameInputLayout.error = getString(R.string.lesson_name_error)
                selectedDate == null -> dialogBinding.selectedDateText.apply {
                    text = getString(R.string.lesson_date_error)
                    setTextColor(requireContext().getColor(R.color.error))
                }
                else -> {
                    viewModel.add(name, selectedDate!!)
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
