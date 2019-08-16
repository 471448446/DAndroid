package com.better.learn.longlive

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.better.learn.longlive.nativeforlive.DaemonReceiver
import com.better.learn.longlive.nativeforlive.DaemonServices
import com.better.learn.longlive.nativeforlive.PersistentReceiver
import com.better.learn.longlive.nativeforlive.PersistentServices
import com.marswin89.marsdaemon.DaemonClient
import com.marswin89.marsdaemon.DaemonConfigurations


object KeepLiveImpl {
    //https://github.com/Marswin/MarsDaemon
    fun initDaemonClient(context: Context) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            DaemonClient(getConfigurations()).onAttachBaseContext(context)
        }
        context.startService(Intent(context, PersistentServices::class.java))
    }

    private fun getConfigurations(): DaemonConfigurations = DaemonConfigurations(
        DaemonConfigurations.DaemonConfiguration(
            "${BuildConfig.APPLICATION_ID}:service",
            PersistentServices::class.java.canonicalName, DaemonServices::class.java.canonicalName
        ),
        DaemonConfigurations.DaemonConfiguration(

            "${BuildConfig.APPLICATION_ID}:daemon",
            PersistentReceiver::class.java.canonicalName, DaemonReceiver::class.java.canonicalName
        ),
        object : DaemonConfigurations.DaemonListener {
            override fun onPersistentStart(context: Context?) {
                Log.e("Daemon", "Native_onPersistentStart")
            }

            override fun onDaemonAssistantStart(context: Context?) {
                Log.e("Daemon", "Native_onDaemonAssistantStart")
            }

            override fun onWatchDaemonDaed() {
                Log.e("Daemon", "Native_onWatchDaemonDaed")
            }
        }
    )
}