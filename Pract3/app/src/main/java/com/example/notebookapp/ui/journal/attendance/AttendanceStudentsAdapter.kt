package com.example.notebookapp.ui.journal.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notebookapp.databinding.ItemAttendanceStudentBinding
import com.example.notebookapp.viewmodel.StudentWithAttendance

class AttendanceStudentsAdapter(
    private val onAttendanceChange: (studentId: Long, isPresent: Boolean) -> Unit
) : ListAdapter<StudentWithAttendance, AttendanceStudentsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAttendanceStudentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemAttendanceStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StudentWithAttendance) {
            binding.studentName.text = item.student.name
            binding.isPresent.setOnCheckedChangeListener(null)
            binding.isPresent.isChecked = item.isPresent
            binding.isPresent.setOnCheckedChangeListener { _, checked ->
                onAttendanceChange(item.student.id, checked)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<StudentWithAttendance>() {
        override fun areItemsTheSame(
            oldItem: StudentWithAttendance,
            newItem: StudentWithAttendance
        ) = oldItem.student.id == newItem.student.id

        override fun areContentsTheSame(
            oldItem: StudentWithAttendance,
            newItem: StudentWithAttendance
        ) = oldItem == newItem
    }
}
