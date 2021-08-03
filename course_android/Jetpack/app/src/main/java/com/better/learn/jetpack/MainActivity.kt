package com.better.learn.jetpack

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.better.learn.jetpack.databinding.ActivityMainBinding
import com.better.learn.jetpack.start_activity_result.StartedActivity

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        jStartActivityForResult()
    }

    private fun jStartActivityForResult() {
        // 1. 获取所有结果 ActivityResultContracts.StartActivityForResult()
        // 2. 只获取url：ActivityResultContracts.GetContent()
        val startActivity =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    val back = result.data?.getStringExtra("haha") ?: "fail get back string"
                    Toast.makeText(this, back, Toast.LENGTH_SHORT).show()
                }
            }

        binding.startActivityForResult.setOnClickListener {
            startActivity.launch(Intent(this, StartedActivity::class.java))
        }
    }
}