package com.better.app.annotation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.better.app.annotation.runtime.InjectUtils
import com.better.app.annotation.runtime.InjectView

class MainActivity : AppCompatActivity() {

    @InjectView(R.id.inject_by_runtime)
    private lateinit var injectViewByRuntime: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InjectUtils.init(this)
        injectViewByRuntime.text = "this is RetentionPolicy.RUNTIME"
    }
}