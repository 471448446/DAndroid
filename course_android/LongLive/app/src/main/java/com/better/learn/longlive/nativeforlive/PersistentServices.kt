package com.better.learn.longlive.nativeforlive

import android.app.Service
import android.content.Intent
import android.os.IBinder

// 业务逻辑服务
class PersistentServices : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}