package com.example.reddittopposts

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reddittopposts.databinding.ActivityMainBinding
import com.example.reddittopposts.util.PostsAdapter
import com.example.reddittopposts.viewmodel.RedditViewModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.Q], manifest = Config.NONE)
class MainActivityTest {

    private lateinit var activity: MainActivity
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: RedditViewModel

    @RelaxedMockK
    private lateinit var adapter: PostsAdapter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        activity = Robolectric.buildActivity(MainActivity::class.java).create().get()

        binding = mockk(relaxed = true)
        viewModel = mockk(relaxed = true)

        every { activity.layoutInflater.inflate(any<Int>(), any<ViewGroup>(), any()) } returns binding.root
        every { ViewModelProvider(activity).get(RedditViewModel::class.java) } returns viewModel
    }


    @Test
    fun activityShouldRequestPermissionsOnCreate() {
        verify { activity.requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), any()) }
    }

    @Test
    fun openImageShouldStartActivityWithIntent() {
        val url = "http://example.com/image.jpg"
        every { activity.startActivity(any()) } just Runs

        activity.openImage(url)

        verify {
            val expectedIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            activity.startActivity(match { intent ->
                intent.action == Intent.ACTION_VIEW && intent.data == Uri.parse(url)
            })
        }
    }

    @Test
    fun saveImageToGalleryShouldHandlePermissionsAndSaveImage() {
        val url = "http://example.com/image.jpg"
        mockkStatic(Uri::class)
        every { Uri.parse(url) } returns Uri.parse(url)

        activity.saveImageToGallery(url)

        verify(exactly = 1) { Thread(any<Runnable>()).start() }
    }

    @Test
    fun verifyRecyclerViewSetup() {
        verify {
            binding.recyclerView.layoutManager = ofType<LinearLayoutManager>()
            binding.recyclerView.adapter = adapter
        }
    }
}
