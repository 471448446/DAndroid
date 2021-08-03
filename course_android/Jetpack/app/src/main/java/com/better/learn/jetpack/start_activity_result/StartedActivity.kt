package com.better.learn.jetpack.start_activity_result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.better.learn.jetpack.databinding.ActivityStartedBinding

//https://developer.android.com/training/basics/intents/result?hl=zh-cn#register
class StartedActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityStartedBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.deliverResult.setOnClickListener {
            setResult(RESULT_OK, Intent().apply {
                putExtra("haha", "this is result string")
            })
            finish()
        }
    }
}