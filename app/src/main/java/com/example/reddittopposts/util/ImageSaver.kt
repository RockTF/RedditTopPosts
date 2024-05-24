package com.example.reddittopposts.util

import android.content.Context
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import java.io.OutputStream
import java.net.URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ImageSaver {
    fun saveImageToGallery(context: Context, imageUrl: String, title: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "$title.jpg")
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }

                val imageUri: Uri? = context.contentResolver.insert(imageCollection, contentValues)
                context.contentResolver.openOutputStream(imageUri!!)?.use { outputStream ->
                    URL(imageUrl).openStream().use { inputStream ->
                        inputStream.copyTo(outputStream)
                        outputStream.flush()
                        showToast(context, "Image saved successfully", Toast.LENGTH_LONG)
                    }
                }
            } catch (e: Exception) {
                showToast(context, "Failed to save image: ${e.localizedMessage}", Toast.LENGTH_LONG)
            }
        }
    }

    private fun showToast(context: Context, message: String, length: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, message, length).show()
        }
    }
}
