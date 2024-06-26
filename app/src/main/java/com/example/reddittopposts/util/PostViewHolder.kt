package com.example.reddittopposts.util

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.reddittopposts.databinding.PostItemBinding
import com.example.reddittopposts.model.RedditPost
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

open class PostViewHolder(
    private val binding: PostItemBinding,
    private val onImageClick: (String) -> Unit,
    private val onImageLongClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    open fun bind(post: RedditPost) {
        binding.author.text = post.author
        binding.postDate.text = "Posted ${formatRelativeDate(post.createdUtc)}"
        Picasso.get().load(post.thumbnail).into(binding.thumbnail)
        binding.numComments.text = "${post.numComments} comments"

        binding.thumbnail.setOnClickListener {
            onImageClick(post.url)
        }

        binding.thumbnail.setOnLongClickListener {
            onImageLongClick(post.url)
            true
        }
    }

    private fun formatRelativeDate(timeInSeconds: Long): String {
        val date = Date(timeInSeconds * 1000)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }
}

