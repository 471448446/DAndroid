package com.better.app.learnshadow

import android.app.Application
import com.better.app.framework.SharePre
import com.better.app.plugin_host_lib.InitApplication

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SharePre.init(this)
        //插件运行在:plugin进程中，这里存取SP有个跨进程的问题，先忽略
        SharePre.setString("host", "zhangshan")
        // 观看启动的Activity
        registerActivityLifecycleCallbacks(LifecycleCallback())
        InitApplication.onApplicationCreate(this)
    }
}