package com.example.notebookapp.ui.journal.students

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notebookapp.R
import com.example.notebookapp.databinding.DialogAddStudentBinding
import com.example.notebookapp.databinding.FragmentStudentsBinding
import com.example.notebookapp.viewmodel.StudentsViewModel
import com.example.notebookapp.viewmodel.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class StudentsFragment : Fragment() {

    private var _binding: FragmentStudentsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StudentsViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application)
    }
    private lateinit var adapter: StudentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = StudentsAdapter { student ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.student_delete_title)
                .setMessage(R.string.student_delete_message)
                .setPositiveButton(R.string.action_delete) { _, _ -> viewModel.delete(student.id) }
                .setNegativeButton(R.string.action_cancel, null)
                .show()
        }

        binding.studentsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.studentsRecycler.adapter = adapter

        viewModel.students.observe(viewLifecycleOwner) { students ->
            adapter.submitList(students)
            binding.emptyView.visibility = if (students.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.addStudentFab.setOnClickListener { showAddDialog() }
    }

    private fun showAddDialog() {
        val dialogBinding = DialogAddStudentBinding.inflate(LayoutInflater.from(requireContext()))

        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.TransparentDialog)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.cancelButton.setOnClickListener { dialog.dismiss() }
        dialogBinding.saveButton.setOnClickListener {
            val name = dialogBinding.nameInput.text?.toString()?.trim() ?: ""
            if (name.isNotEmpty()) {
                viewModel.add(name)
                dialog.dismiss()
            } else {
                dialogBinding.nameInputLayout.error = getString(R.string.student_name_error)
            }
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
