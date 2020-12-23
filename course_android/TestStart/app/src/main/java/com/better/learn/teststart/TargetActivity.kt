package com.better.learn.teststart

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_target.*
import kotlin.random.Random

class TargetActivity : AppCompatActivity() {
    companion object {
        const val TAG = "Better"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target)
        val extra = intent.getStringExtra(TAG)?.run {
            "success get extra $this"
        } ?: "fail get extra"
        textView.text = extra + "\n" + Random.nextInt().toString()
        Log.e("Better","TargetActivity create()")
    }

    @SuppressLint("SetTextI18n")
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val extra = intent?.getStringExtra(TAG)?.run {
            "success get extra $this"
        } ?: "fail get extra"
        textView.text = extra + "\n" + Random.nextInt().toString()
        Log.e("Better","TargetActivity onNewIntent()")
    }
}