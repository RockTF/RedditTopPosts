package com.example.reddittopposts.util

import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.reddittopposts.databinding.PostItemBinding
import com.example.reddittopposts.model.RedditPost
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.text.SimpleDateFormat
import java.util.*

@RunWith(RobolectricTestRunner::class)
class PostViewHolderTest {

    private lateinit var binding: PostItemBinding
    private lateinit var viewHolder: PostViewHolder
    private lateinit var requestCreator: RequestCreator
    private val onImageClick: (String) -> Unit = mockk(relaxed = true)
    private val onImageLongClick: (String) -> Unit = mockk(relaxed = true)

    @Before
    fun setUp() {
        val mockAuthor: TextView = mockk(relaxed = true)
        val mockPostDate: TextView = mockk(relaxed = true)
        val mockNumComments: TextView = mockk(relaxed = true)
        val mockThumbnail: ImageView = mockk(relaxed = true)
        val mockRoot: CardView = mockk(relaxed = true)

        binding = mockk(relaxed = true) {
            every { author } returns mockAuthor
            every { postDate } returns mockPostDate
            every { numComments } returns mockNumComments
            every { thumbnail } returns mockThumbnail
            every { root } returns mockRoot
        }

        mockkStatic(Picasso::class)
        val picasso = mockk<Picasso>()
        requestCreator = mockk(relaxed = true)
        every { Picasso.get() } returns picasso
        every { picasso.load(any<String>()) } returns requestCreator
        every { requestCreator.into(any<ImageView>()) } answers { Unit }

        viewHolder = PostViewHolder(binding, onImageClick, onImageLongClick)
    }


    @Test
    fun bind_setsData() {
        val redditPost = RedditPost(
            author = "author1",
            createdUtc = 12345L,
            thumbnail = "thumbnailUrl",
            numComments = 10,
            url = "postUrl"
        )

        viewHolder.bind(redditPost)

        verify { binding.author.text = redditPost.author }
        verify { binding.postDate.text = "Posted 1970-01-01 03:00:45" }
        verify { binding.numComments.text = "${redditPost.numComments} comments" }

        verify { Picasso.get().load(redditPost.thumbnail) }
        verify { requestCreator.into(any<ImageView>()) }

        verify { binding.thumbnail.performClick() }
        verify { onImageClick.invoke(redditPost.url) }
        verify { binding.thumbnail.performLongClick() }
        verify { onImageLongClick.invoke(redditPost.url) }
    }


    private fun formatRelativeDate(timeInSeconds: Long): String {
        val date = Date(timeInSeconds * 1000)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }
}
