package com.better.learn.jetpack.app_startup

import android.util.Log

const val TAG = "AppStartUp"

// 需要被初始化的类

class Age(private val age: Int) {
    init {
        Log.i(TAG, "Initial Age $age")
    }
}

class Name(private val name: String) {
    init {
        Log.i(TAG, "Initial Name $name")
    }
}

class Man(private val name: String) {
    init {
        Log.i(TAG, "Initial Man $name")
    }
}

class Woman(private val name: String) {
    init {
        Log.i(TAG, "Initial Woman $name")
    }
}