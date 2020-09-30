package com.better.learn.matrix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_evil_method.setOnClickListener {
            Thread.sleep(2000)
        }
        button_anr.setOnClickListener {
            Thread.sleep(10000)
            // 滑动屏幕
        }
    }
}