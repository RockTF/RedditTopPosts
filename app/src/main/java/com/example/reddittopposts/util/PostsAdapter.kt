package com.example.reddittopposts.util

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reddittopposts.MainActivity
import com.example.reddittopposts.databinding.PostItemBinding
import com.example.reddittopposts.model.RedditPost
import com.squareup.picasso.Picasso

class PostsAdapter(private val activity: MainActivity) : ListAdapter<RedditPost, PostsAdapter.PostViewHolder>(
    DiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(post: RedditPost) {
            binding.author.text = post.author
            binding.postDate.text = "Posted ${formatRelativeDate(post.createdUtc)}"
            binding.numComments.text = "${post.numComments} comments"
            Picasso.get().load(post.thumbnail).into(binding.thumbnail)

            binding.thumbnail.setOnClickListener {
                openImageInBrowser(post.url)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<RedditPost>() {
        override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean = oldItem.url == newItem.url
        override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean = oldItem == newItem
    }

    private fun openImageInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(intent)
    }

    private fun formatRelativeDate(utc: Long): String {
        val now = System.currentTimeMillis()
        val secondsAgo = (now / 1000) - utc
        val hoursAgo = secondsAgo / 3600
        return "$hoursAgo hours ago"
    }
}
