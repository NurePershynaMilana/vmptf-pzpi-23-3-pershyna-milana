package com.example.notebookapp.ui.journal.lessons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notebookapp.data.entities.Lesson
import com.example.notebookapp.databinding.ItemLessonBinding
import com.example.notebookapp.util.DateFormatter

class LessonsAdapter(
    private val onDeleteClick: (Lesson) -> Unit
) : ListAdapter<Lesson, LessonsAdapter.LessonViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val binding = ItemLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LessonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LessonViewHolder(private val binding: ItemLessonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lesson: Lesson) {
            binding.lessonName.text = lesson.name
            binding.lessonDate.text = DateFormatter.format(lesson.date)
            binding.deleteButton.setOnClickListener { onDeleteClick(lesson) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Lesson>() {
        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson) = oldItem == newItem
    }
}
