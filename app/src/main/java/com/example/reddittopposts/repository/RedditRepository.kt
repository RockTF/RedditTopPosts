package com.example.reddittopposts.repository

import com.example.reddittopposts.api.RedditApi

class RedditRepository(private val redditApi: RedditApi) {
    suspend fun getTopPosts(limit: Int) = redditApi.getTopPosts(limit)
}