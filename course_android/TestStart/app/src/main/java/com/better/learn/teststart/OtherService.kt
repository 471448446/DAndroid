package com.better.learn.teststart

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

class OtherService : Service() {
    companion object {
        const val action = "com.better.receiver.one"
        fun send(context: Context) {
            context.sendBroadcast(Intent(action), "com.better.fucker")
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(applicationContext, "permission ok", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        MainActivity.printPermission(applicationContext)
        Log.e(
            "Better",
            "other check permission ${
                ActivityCompat.checkSelfPermission(
                    this,
                    "com.better.fucker"
                ) == PackageManager.PERMISSION_GRANTED
            }"
        )
        registerReceiver(receiver, IntentFilter(action), "com.better.fucker", null)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}