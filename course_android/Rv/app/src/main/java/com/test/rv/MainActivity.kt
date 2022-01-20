package com.test.rv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.rv.d1.Demo1Activity
import com.test.rv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.demo1.setOnClickListener {
            startActivity(Intent(this, Demo1Activity::class.java))
        }
    }
}