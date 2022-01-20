package com.test.hilt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * [@HiltAndroidApp]
 * [@AndroidEntryPoint]
 * [@Inject]
 * 这三个就可以简单的运行
 */
@AndroidEntryPoint
class HelloWorldActivity : AppCompatActivity() {
    /**
     * 注入User，这里使用User的默认构造方法
     */
    @Inject
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)
        Log.d("Better", user.toString())
    }
}