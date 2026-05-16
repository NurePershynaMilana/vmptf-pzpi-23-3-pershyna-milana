package com.example.notebookapp.ui.journal.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notebookapp.data.entities.Lesson
import com.example.notebookapp.databinding.FragmentAttendanceBinding
import com.example.notebookapp.util.DateFormatter
import com.example.notebookapp.viewmodel.AttendanceViewModel
import com.example.notebookapp.viewmodel.ViewModelFactory

class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AttendanceViewModel by activityViewModels {
        ViewModelFactory(requireActivity().application)
    }

    private lateinit var studentsAdapter: AttendanceStudentsAdapter
    private var selectedLessonId: Long? = null
    private var lessonsList: List<Lesson> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        studentsAdapter = AttendanceStudentsAdapter { studentId, isPresent ->
            val lessonId = selectedLessonId ?: return@AttendanceStudentsAdapter
            viewModel.setAttendance(studentId, lessonId, isPresent)
        }

        binding.studentsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.studentsRecycler.adapter = studentsAdapter

        viewModel.lessons.observe(viewLifecycleOwner) { lessons ->
            lessonsList = lessons
            val labels = lessons.map { "${it.name} — ${DateFormatter.format(it.date)}" }
            val dropdownAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                labels
            )
            binding.lessonSelector.setAdapter(dropdownAdapter)
        }

        binding.lessonSelector.setOnItemClickListener { _, _, position, _ ->
            val lesson = lessonsList.getOrNull(position) ?: return@setOnItemClickListener
            selectedLessonId = lesson.id
            viewModel.selectLesson(lesson.id)
            binding.noLessonHint.visibility = View.GONE
            binding.studentsRecycler.visibility = View.VISIBLE
        }

        viewModel.studentsWithAttendance.observe(viewLifecycleOwner) { list ->
            studentsAdapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
