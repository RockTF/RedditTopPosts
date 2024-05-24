package com.example.reddittopposts.repository

import android.util.Log
import com.example.reddittopposts.model.RedditPost
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RedditRepository {

    companion object {
        private const val TAG = "RedditRepository"
        private const val REDDIT_URL = "https://www.reddit.com/r/all/top.json?limit=50"
    }

    fun fetchPosts(): List<RedditPost> {
        val posts = mutableListOf<RedditPost>()
        var urlConnection: HttpURLConnection? = null

        try {
            val url = URL(REDDIT_URL)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connect()

            val inputStream = urlConnection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))

            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append('\n')
            }

            val jsonResponse = stringBuilder.toString()
            val jsonObject = JSONObject(jsonResponse)
            val jsonArray = jsonObject.getJSONObject("data").getJSONArray("children")

            for (i in 0 until jsonArray.length()) {
                val postObject = jsonArray.getJSONObject(i).getJSONObject("data")
                val post = parseRedditPost(postObject)
                posts.add(post)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching Reddit posts", e)
        } finally {
            urlConnection?.disconnect()
        }

        return posts
    }

    private fun parseRedditPost(jsonObject: JSONObject): RedditPost {
        return RedditPost(
            author = jsonObject.optString("author"),
            createdUtc = jsonObject.optLong("created_utc"),
            thumbnail = jsonObject.optString("thumbnail"),
            numComments = jsonObject.optInt("num_comments"),
            url = jsonObject.optString("url"),
            title = jsonObject.optString("title")
        )
    }
}
