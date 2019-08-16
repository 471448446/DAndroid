package com.better.learn.longlive.nativeforlive

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder

class PersistentReceiver : Service() {
    override fun onBind(intent: Intent?)= null
}