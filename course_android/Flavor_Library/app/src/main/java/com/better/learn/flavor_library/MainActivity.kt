package com.better.learn.flavor_library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.better.learn.library.AdData

/**
 *
 * 选择library中的不同flavor
 * https://developer.android.com/studio/build/dependencies#resolve_matching_errors
 * https://proandroiddev.com/advanced-android-flavors-part-3-flavorful-libraries-6563eec5a187
 * https://stackoverflow.com/questions/49815655/include-library-with-flavor-android
 * @author Better
 * @date 2021/9/18 16:12
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.show_ad).setOnClickListener {
            val adData = AdData()
            adData.showAd(this@MainActivity)
        }
    }
}