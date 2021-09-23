package com.better.learn.flavor_library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.better.learn.library.AdData


/**
 *
 * 通过missingDimensionStrategy来配置选择libary中的哪个flavor
 * @author Better
 * @date 2021/9/23 11:24
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