package com.example.reddittopposts.model

data class RedditPost(
    val author: String,
    val createdUtc: Long,
    val thumbnail: String?,
    val numComments: Int,
    val url: String,
    val title: String
)
