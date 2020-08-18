package com.better.learn.scopedstorage

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_load_pictures.*
import java.util.ArrayList

/**
 * https://developer.android.com/training/data-storage/use-cases#show-all-folder
 * 只要有存储权限，就可以访问其他应用创建的文件
 */
class LoadPicturesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_pictures)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            loadPictures()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && default(false) {
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            }) {
            loadPictures()
        }
    }

    private fun loadPictures() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return
        }
        var count = 0
        val pictures: MutableList<ImageInfo> = ArrayList()
        val external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DISPLAY_NAME
        )
        contentResolver.query(
            external,
            projection, null, null, null
        )?.use { cursor ->
            Log.e(MainActivity.TAG, "${cursor.count}")
            while (cursor.moveToNext()) {
                if (count > 50) {
                    break
                }
                count++
                val id = cursor.getLong(0)
                val path = cursor.getString(1)
                // 判断 是否包含路径 Pictures/test 只的读取文件夹下的图片
                val uri =
                    ContentUris.withAppendedId(external, id)
                val image = ImageInfo(
                    cursor.getString(2),
                    uri
                )
                Log.i(MainActivity.TAG, "$id,$path,$image")
                pictures.add(image)
            }
        }

        pictures_list.apply {
            layoutManager = GridLayoutManager(this@LoadPicturesActivity, 3)
            adapter = PicturesAdapter(pictures)
        }
    }
}

data class ImageInfo(val name: String, val uri: Uri)

class PicturesAdapter(private val pictures: List<ImageInfo>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val imageView = ImageView(parent.context)
        return ViewHolder(imageView)
    }

    override fun getItemCount(): Int = pictures.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(pictures[position].uri)
            .into(
                (holder.itemView as ImageView)
            )
    }
}