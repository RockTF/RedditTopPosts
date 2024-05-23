package com.example.reddittopposts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reddittopposts.databinding.ActivityMainBinding
import com.example.reddittopposts.util.PostsAdapter
import com.example.reddittopposts.viewmodel.RedditViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: RedditViewModel
    private val storagePermissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), storagePermissionCode)
        }

        viewModel = ViewModelProvider(this).get(RedditViewModel::class.java)
        val adapter = PostsAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.posts.observe(this) { posts ->
            adapter.submitList(posts.toList())
        }

        viewModel.loadTopPosts()
    }

    fun openImage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    fun saveImageToGallery(imageUrl: String) {
        val thread = Thread {
            try {
                val url = URL(imageUrl)
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

