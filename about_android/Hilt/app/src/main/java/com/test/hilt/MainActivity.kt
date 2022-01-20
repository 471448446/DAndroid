package com.test.hilt

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * https://developer.android.com/training/dependency-injection/manual
 * [https://medium.com/androiddevelopers/dependency-injection-on-android-with-hilt-67b6031e62d]
 * [https://miro.medium.com/max/2000/0*bmqjwH1MSCkour_u.png]
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    /**
     * 这种情况有点注意的是，可能viewModel需要一些额外的参数。使用可以通过viewModels的Fatory来自定义创建的。
     * 但是如果自己创建就达不到自动注入的效果了。
     * 所以额外的参数可以通过viewModel的一个方法传进入
     * viewModel.setXXXArgs(xxx)
     */
    private val viewModel: MainViewMode by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.textView).text = viewModel.user.name
    }
}