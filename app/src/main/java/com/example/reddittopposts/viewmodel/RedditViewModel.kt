package com.example.reddittopposts.viewmodel

import androidx.lifecycle.*
import com.example.reddittopposts.api.RedditApi
import com.example.reddittopposts.model.RedditPost
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.launch

class RedditViewModel : ViewModel() {
    private var after: String? = null
    private val _posts = MutableLiveData<List<RedditPost>>()
    val posts: LiveData<List<RedditPost>> = _posts

    private val redditApi: RedditApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.reddit.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RedditApi::class.java)
    }

    fun loadTopPosts(limit: Int = 50) {
        viewModelScope.launch {
            val response = redditApi.getTopPosts(limit, after)
            after = response.data.after
            _posts.value = response.data.children.map { it.data }
        }
    }
}
