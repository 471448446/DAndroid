package com.better.learn.scopedstorage

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import java.lang.Exception

fun loadImageFromUri(uri: Uri?, imageView: ImageView) {
    try {
        uri?.let {
            val fd = imageView.context.contentResolver.openFileDescriptor(it, "r")
            fd?.use {
                imageView.setImageBitmap(BitmapFactory.decodeFileDescriptor(fd.fileDescriptor))
            }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            val bitmap = imageView.context.contentResolver.loadThumbnail(uri, Size(60, 60), null)
//            imageView.setImageBitmap(bitmap)
//        }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(imageView.context, "fail open image", Toast.LENGTH_SHORT).show()
    }
}