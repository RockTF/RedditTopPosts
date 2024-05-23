package com.example.reddittopposts.api

import com.example.reddittopposts.model.RedditResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApi {
    @GET("top.json")
    suspend fun getTopPosts(@Query("limit") limit: Int, @Query("after") after: String? = null): Response<RedditResponse>
}