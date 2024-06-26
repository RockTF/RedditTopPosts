package com.example.reddittopposts.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.reddittopposts.databinding.PostItemBinding
import com.example.reddittopposts.model.RedditPost
import com.example.reddittopposts.MainActivity
import com.example.reddittopposts.util.PostViewHolder

class PostsAdapter(private val activity: MainActivity) : ListAdapter<RedditPost, PostViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding,
            onImageClick = { url -> activity.openImage(url) },
            onImageLongClick = { url -> activity.saveImageToGallery(url) }
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<RedditPost>() {
        override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean = oldItem.url == newItem.url
        override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean = oldItem == newItem
    }
}
