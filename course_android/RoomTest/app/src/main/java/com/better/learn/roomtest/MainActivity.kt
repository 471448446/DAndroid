package com.better.learn.roomtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread {
            val userDao = AppDatabase.shared.getUserDao()
            (0 until 5).forEach {
                userDao.save(UserBeanA("name:$it"))
                userDao.save(UserBeanB("name:$it"))
            }
            Log.e(
                "Better", "result=${userDao.getUsersA().size},${userDao.getUsersB().size}"
            )
        }.start()

    }
}
