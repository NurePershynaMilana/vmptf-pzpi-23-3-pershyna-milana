package com.example.notebookapp.ui.journal.students

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notebookapp.data.entities.Student
import com.example.notebookapp.databinding.ItemStudentBinding

class StudentsAdapter(
    private val onDeleteClick: (Student) -> Unit
) : ListAdapter<Student, StudentsAdapter.StudentViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StudentViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student) {
            binding.studentName.text = student.name
            binding.deleteButton.setOnClickListener { onDeleteClick(student) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Student, newItem: Student) = oldItem == newItem
    }
}
