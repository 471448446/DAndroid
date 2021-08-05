package com.better.learn.jetpack.app_startup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.startup.AppInitializer
import com.better.learn.jetpack.databinding.ActivityInitlializeBinding

class InitializeActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityInitlializeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            AppInitializer
                .getInstance(this)
                .initializeComponent(InitializesMan::class.java)
        }
    }
}