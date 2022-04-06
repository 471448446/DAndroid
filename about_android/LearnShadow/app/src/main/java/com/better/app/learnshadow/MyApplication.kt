package com.better.app.learnshadow

import android.app.Application
import com.better.app.plugin_host_lib.InitApplication

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 观看启动的Activity
        registerActivityLifecycleCallbacks(LifecycleCallback())
        InitApplication.onApplicationCreate(this)
    }
}