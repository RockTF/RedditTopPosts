package com.example.reddittopposts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reddittopposts.model.RedditPost
import com.example.reddittopposts.repository.RedditRepository
import kotlinx.coroutines.launch

class RedditViewModel(private val repository: RedditRepository) : ViewModel() {
    private val _posts = MutableLiveData<List<RedditPost>>()
    val posts: LiveData<List<RedditPost>> = _posts

    fun fetchTopPosts(limit: Int = 10) {
        viewModelScope.launch {
            val response = repository.getTopPosts(limit)
            if (response.isSuccessful) {
                _posts.postValue(response.body()?.data?.children?.map { it.data })
            }
        }
    }
}
