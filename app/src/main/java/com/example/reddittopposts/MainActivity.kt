package com.example.reddittopposts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.reddittopposts.ui.theme.RedditTopPostsTheme
import com.example.reddittopposts.model.RedditPost
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedditTopPostsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PostList(
                        posts = listOf(
                            RedditPost("Author1", System.currentTimeMillis() / 1000 - 12000, "https://example.com/image1.jpg", 100, "https://example.com/post1"),
                            RedditPost("Author2", System.currentTimeMillis() / 1000 - 36000, "https://example.com/image2.jpg", 150, "https://example.com/post2")
                        ),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PostList(posts: List<RedditPost>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(posts) { post ->
            PostItem(post)
        }
    }
}

@Composable
fun PostItem(post: RedditPost) {
    Box(modifier = Modifier.padding(8.dp)) {
        Text(text = "${post.author} - ${getTimeAgo(post.createdUtc)} - ${post.numComments} comments", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!", style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun getTimeAgo(time: Long): String {
    val now = System.currentTimeMillis() / 1000
    val diff = now - time
    return when {
        diff < 60 -> "Just now"
        diff < 3600 -> "${TimeUnit.SECONDS.toMinutes(diff)} minutes ago"
        diff < 86400 -> "${TimeUnit.SECONDS.toHours(diff)} hours ago"
        else -> "${TimeUnit.SECONDS.toDays(diff)} days ago"
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RedditTopPostsTheme {
        Greeting("Android")
    }
}
