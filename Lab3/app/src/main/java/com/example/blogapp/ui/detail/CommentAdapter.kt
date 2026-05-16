package com.example.blogapp.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapp.data.relations.CommentWithAuthor
import com.example.blogapp.databinding.ItemCommentBinding
import com.example.blogapp.util.DateFormatter

class CommentAdapter : ListAdapter<CommentWithAuthor, CommentAdapter.CommentViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<CommentWithAuthor>() {
        override fun areItemsTheSame(oldItem: CommentWithAuthor, newItem: CommentWithAuthor): Boolean =
            oldItem.comment.id == newItem.comment.id

        override fun areContentsTheSame(oldItem: CommentWithAuthor, newItem: CommentWithAuthor): Boolean =
            oldItem == newItem
    }

    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommentWithAuthor) {
            binding.commentAuthor.text = item.authorUsername
            binding.commentText.text = item.comment.text
            binding.commentDate.text = DateFormatter.format(item.comment.createdAt)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
