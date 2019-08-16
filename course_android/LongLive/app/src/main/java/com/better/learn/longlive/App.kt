package com.better.learn.longlive

import android.app.Application
import android.content.Context

class App : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        KeepLiveImpl.initDaemonClient(this)
    }
}