package com.example.blogapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapp.data.relations.PostWithDetails
import com.example.blogapp.databinding.ItemPostBinding
import com.example.blogapp.util.DateFormatter

class PostAdapter(
    private val onClick: (PostWithDetails) -> Unit,
    private val onLongClick: (PostWithDetails) -> Unit
) : ListAdapter<PostWithDetails, PostAdapter.PostViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<PostWithDetails>() {
        override fun areItemsTheSame(oldItem: PostWithDetails, newItem: PostWithDetails): Boolean =
            oldItem.post.id == newItem.post.id

        override fun areContentsTheSame(oldItem: PostWithDetails, newItem: PostWithDetails): Boolean =
            oldItem == newItem
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PostWithDetails) {
            binding.postItemTitle.text = item.post.title
            binding.postItemPreview.text = item.post.content.take(100)
            binding.postItemMeta.text = "${item.categoryName} • ${item.authorUsername} • ${DateFormatter.format(item.post.createdAt)}"
            binding.root.setOnClickListener { onClick(item) }
            binding.root.setOnLongClickListener { onLongClick(item); true }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
