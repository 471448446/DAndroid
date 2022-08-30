package com.better.learn.sharedmem

import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Messenger
import android.os.SharedMemory
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi

class OtherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            val binderProxy = intent.extras?.getBinder("hello")
            if (null == binderProxy) {
                Log.d("Better", "shit null binder")
                return
            }
            val binder = IMyAidlInterface.Stub.asInterface(binderProxy)
            handleData(binder.get())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    private fun handleData(sm: SharedMemory) {
        val imageView = findViewById<ImageView>(R.id.imageview)
        val byteBuffer = sm.mapReadOnly()
        // 获取数据
        val len = byteBuffer.limit() - byteBuffer.position()
        val byteArray = ByteArray(len)
        byteBuffer.get(byteArray)
        // 转换
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, len)
        imageView.setImageBitmap(bitmap)
        // 释放
        SharedMemory.unmap(byteBuffer)
        sm.close()
    }
}