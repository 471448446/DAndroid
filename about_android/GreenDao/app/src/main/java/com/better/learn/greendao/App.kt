package com.better.learn.greendao

import android.app.Application
import android.util.Log
import com.better.learn.greendao.entity.daoSession

class App : Application() {
    companion object {
        lateinit var shared: App
    }

    override fun onCreate() {
        super.onCreate()
        shared = this
        Log.e("Better", "${daoSession.allDaos.size}")
    }
}