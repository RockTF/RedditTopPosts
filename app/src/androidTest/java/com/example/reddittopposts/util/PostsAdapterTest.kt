package com.example.reddittopposts.util

import android.view.ViewGroup
import com.example.reddittopposts.databinding.PostItemBinding
import com.example.reddittopposts.model.RedditPost
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PostsAdapterTest {

    private lateinit var parent: ViewGroup
    private lateinit var binding: PostItemBinding
    private lateinit var postActions: IPostActions
    private lateinit var adapter: PostsAdapter

    @Before
    fun setUp() {
        parent = mock()
        binding = mock { on { root } doReturn mock() }
        postActions = mock()
        adapter = PostsAdapter(postActions)
    }

    @Test
    fun onBindViewHolder_bindsData() {
        val redditPost = RedditPost(
            "author1",
            12345L,
            "thumbnailUrl",
            10,
            "postUrl"
        )
        adapter.submitList(listOf(redditPost))

        val viewHolder = mock<PostViewHolder> { on { bind(any()) } doAnswer {} }
        adapter.onBindViewHolder(viewHolder, 0)

        verify(viewHolder).bind(redditPost)
    }
}
