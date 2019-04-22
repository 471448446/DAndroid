package com.example.better.coroutines

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 * https://www.androidauthority.com/kotlin-coroutines-asynchronous-programming-858566/
 * Learning any new technology takes time and effort, so before taking the plunge you’ll want to know what’s in it for you
 * 1. 轻量级的线程，不会阻塞，即可以同时创建多个异步任务。
 * 2. 异步函数式编程，避免写回调，使得异步打的逻辑可以按照自上而下的顺序来写（以前用RXJava）
 *
 * https://codelabs.developers.google.com/codelabs/kotlin-coroutines/#0
 * Coroutines are a Kotlin feature that convert async callbacks for long-running tasks,
 * such as database or network access, into sequential code.
 * Create by Better 2018/11/12 15:28
 */
class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "Better"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        coroutinesFirst()
        coroutinesAsync()
        cancelCoroutines()
    }

    private fun cancelCoroutines() {
        val o = launch {
            log("cancelCoroutines() run")
            delay(4000)
            log("cancel fail")
        }
        log("cancelCoroutines() cancel")
        // 会抛异常
//        o.cancel(RuntimeException("自主取消"))
        // 只取消
        o.cancel()
    }

    private fun coroutinesAsync() {
        launch {
            val result = async {
                delay(2000)
                return@async "Hello"
            }.await()
            log(result)
        }
        log("async() 2 秒显示结果")
    }

    private fun coroutinesFirst() {
        launch(UI) {
            delay(5000)
            toast("coroutinesFirst() Hello Coroutines")
        }
        log("coroutinesFirst() 5 秒后Toast")
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    private fun log(msg: String) = Log.d(TAG, msg)
}
