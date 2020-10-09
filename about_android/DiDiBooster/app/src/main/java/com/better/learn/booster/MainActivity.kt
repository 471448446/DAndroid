package com.better.learn.booster

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * https://github.com/didi/booster
 * ./gradlew assembleRelease
 * ./gradlew analyseRelease
 * find build/reports -name '*.dot' | xargs -t -I{} dot -O -Tpng {}
 */
class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "Booster"
    }

    private val threadPool: ExecutorService by lazy {
        Executors.newFixedThreadPool(2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 线程默认加上了名称
        testThread()
        testThreadPool()
        // Sp 被替换
        testSpApply()
        testSpCommit()
    }

    private fun testThreadPool() {
        threadPool.execute {
            Thread.sleep(1000)
            Log.d(TAG, "Hello Thread ${Thread.currentThread().name}")
        }
    }

    private fun testSpCommit() {
        val sp = getSharedPreferences("hello world", Context.MODE_PRIVATE)
        sp.edit().putLong("time", System.currentTimeMillis()).commit()
    }

    private fun testSpApply() {
        val sp = getSharedPreferences("hello world", Context.MODE_PRIVATE)
        sp.edit().putLong("time", System.currentTimeMillis()).apply()
    }

    private fun testThread() {
        Thread({
            Thread.sleep(2000)
            Log.d(TAG, "Hello Thread ${Thread.currentThread().name}")
        }, "MainActivityThread").start()

        Thread {
            Thread.sleep(2000)
            Log.d(TAG, "Hello Thread ${Thread.currentThread().name}")
        }.start()
    }
}