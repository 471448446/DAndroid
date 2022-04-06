package com.better.app.learnshadow

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

class LifecycleCallback : Application.ActivityLifecycleCallbacks {
    val TAG = "AppLifecycle"
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated ${activity.javaClass.name}")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(TAG, "onActivityStarted ${activity.javaClass.name}")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(TAG, "onActivityResumed ${activity.javaClass.name}")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d(TAG, "onActivityPaused ${activity.javaClass.name}")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(TAG, "onActivityStopped ${activity.javaClass.name}")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.d(TAG, "onActivitySaveInstanceState ${activity.javaClass.name}")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(TAG, "onActivityDestroyed ${activity.javaClass.name}")
    }
}