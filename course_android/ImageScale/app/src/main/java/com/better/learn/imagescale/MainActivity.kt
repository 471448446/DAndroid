package com.better.learn.imagescale

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        lifecycleScope.launch {
//            val bitmap = withContext(Dispatchers.IO) {
//                BitmapFactory.decodeResource(resources, R.drawable.abcd)
//            }
//            Log.i("Better", "bitmap size: ${localPath.width},${localPath.height}")
//            Log.i("Better", "imageView size: ${scale_image_view.width},${scale_image_view.height}")
//            scale_image_view.setImageBitmap(bitmap)
//        }
        scale_image_view.setImageResource(R.drawable.twuk)
    }
}