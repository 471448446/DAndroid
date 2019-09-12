package com.better.learn.roomtest

import android.app.Application

class App : Application() {
    companion object {
        lateinit var shared: App
    }

    override fun onCreate() {
        super.onCreate()
        shared = this
    }
}