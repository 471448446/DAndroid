package com.better.app.plugin1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.better.app.framework.SharePre

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.main_txt)
        textView.text = "hello plugin1 \n host:${SharePre.getString("host")}"
    }
}