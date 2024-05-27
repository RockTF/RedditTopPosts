package com.example.reddittopposts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reddittopposts.databinding.ActivityMainBinding
import com.example.reddittopposts.util.IPostActions
import com.example.reddittopposts.util.PostsAdapter
import com.example.reddittopposts.viewmodel.RedditViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL

class MainActivity : AppCompatActivity(), IPostActions {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: RedditViewModel
    private lateinit var adapter: PostsAdapter
    private val storagePermissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.getString("lastState")?.let {
            binding.textViewState.text = it
        }

        savedInstanceState?.let { bundle ->
            val state = bundle.getParcelable<Parcelable>("recyclerViewState")
            state?.let {
                binding.recyclerView.layoutManager?.onRestoreInstanceState(it)
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), storagePermissionCode)
        }

        setupRecyclerView()
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(RedditViewModel::class.java)
        viewModel.posts.observe(this) { posts ->
            adapter.submitList(posts)
        }
        if (viewModel.posts.value == null) {
            viewModel.loadTopPosts()
        }
    }

    private fun initialize() {
        setupRecyclerView()
        setupViewModel()
    }

    private fun setupRecyclerView() {
        adapter = PostsAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == storagePermissionCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initialize()
        } else {
            Toast.makeText(this, "Permission denied to write to your External storage", Toast.LENGTH_LONG).show()
        }
    }

    override fun openImage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun saveImageToGallery(url: String) {
        val thread = Thread {
            try {
                val url = URL(url)
                val inputStream = url.openStream()
                val outputStream: OutputStream
                val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${System.currentTimeMillis()}.jpg")

                outputStream = FileOutputStream(file)
                outputStream.write(inputStream.readBytes())
                outputStream.close()

                runOnUiThread {
                    Toast.makeText(this, "Image Saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error saving image: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
        thread.start()
    }
}
