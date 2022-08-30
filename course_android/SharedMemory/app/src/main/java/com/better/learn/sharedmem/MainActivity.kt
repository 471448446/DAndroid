package com.better.learn.sharedmem

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.system.OsConstants
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.lang.RuntimeException

/**
 * 跨进程传输大数据
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.start).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                startWork()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    private fun startWork() {
//        Thread {
//            // 先将数据读取内存
//            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test)
//            val byteArrayOutputStream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
//            val bytes = byteArrayOutputStream.toByteArray()
//            bitmap.recycle()
//            // 获取SharedMemory
//            val sharedMemory = SharedMemory.create("hello", bytes.size)
//            // 获取流操作工具
//            val byteBuffer = sharedMemory.mapReadWrite()
//            // 处理数据
//            byteBuffer.put(bytes)
//            // 把sharedMemory权限设置为只读，create默认是读写权限都有，这里可以避免客户端更改数据
//            sharedMemory.setProtect(OsConstants.PROT_READ)
//            // 使用完需要unmap
//            SharedMemory.unmap(byteBuffer)
        /*
 java.lang.RuntimeException: Not allowed to write file descriptors here
 	at android.os.Parcel.nativeWriteFileDescriptor(Native Method)
 	at android.os.Parcel.writeFileDescriptor(Parcel.java:921)
 	at android.os.SharedMemory.writeToParcel(SharedMemory.java:273)
 	at android.os.Parcel.writeParcelable(Parcel.java:1952)
 	at android.os.Parcel.writeValue(Parcel.java:1858)
 	at android.os.Parcel.writeArrayMapInternal(Parcel.java:1023)
 	at android.os.BaseBundle.writeToParcelInner(BaseBundle.java:1627)
 	at android.os.Bundle.writeToParcel(Bundle.java:1307)
 	at android.os.Parcel.writeBundle(Parcel.java:1092)
 	at android.content.Intent.writeToParcel(Intent.java:11210)
 	at android.app.IActivityTaskManager$Stub$Proxy.startActivity(IActivityTaskManager.java:2254)
 	at android.app.Instrumentation.execStartActivity(Instrumentation.java:1745)
        */
//        }.start()
        //

        val intent = Intent(this@MainActivity, OtherActivity::class.java).also {
            it.putExtras(Bundle().apply {
                putBinder("hello", MyBinder())
            })
        }
        startActivity(intent)
    }
}

class MyBinder : IMyAidlInterface.Stub() {
    override fun get(): SharedMemory {
        Log.d("Better", "invoke getBitMap() ${Process.myPid()}")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
            throw RuntimeException("Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1")
        }
        // 先将数据读取内存
        val bitmap = BitmapFactory.decodeResource(App.shared.resources, R.drawable.test)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        bitmap.recycle()
        // 获取SharedMemory
        val sharedMemory = SharedMemory.create("hello", bytes.size)
        // 获取流操作工具
        val byteBuffer = sharedMemory.mapReadWrite()
        // 处理数据
        byteBuffer.put(bytes)
        // 把sharedMemory权限设置为只读，create默认是读写权限都有，这里可以避免客户端更改数据
        sharedMemory.setProtect(OsConstants.PROT_READ)
        // 使用完需要unmap
        SharedMemory.unmap(byteBuffer)
        return sharedMemory
    }
}