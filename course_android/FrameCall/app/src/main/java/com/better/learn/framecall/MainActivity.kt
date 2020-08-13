package com.better.learn.framecall

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Choreographer
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
class MainActivity : AppCompatActivity() {
    var flagWatch = false
    var flagEndWatch = false
    var frame1: Int = 0
    var frame2: Int = 0
    var startTime: Long = 0L
    private val choCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            if (flagEndWatch) {
                return
            }
            frame1++
            Choreographer.getInstance().postFrameCallback(this)
        }
    }
    private val preDrawListener = ViewTreeObserver.OnPreDrawListener {
        if (flagWatch) {
            if (!flagEndWatch) {
                frame2++
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (btn_start.parent as ViewGroup).viewTreeObserver.addOnPreDrawListener(preDrawListener)
        btn_start.setOnClickListener {
            it.isEnabled = false
            //
            startTime = System.currentTimeMillis()
            flagWatch = true
            flagEndWatch = false
            frameChoreo()
            frameDraw()
//            frameDump()
            GlobalScope.launch(Dispatchers.Main) {
                for (i in (0 until 10)) {
                    btn_start.invalidate()
                    delay(200)
                }
                flagEndWatch = true
                flagWatch = false
                val endTime = System.currentTimeMillis()
                val duration = (endTime - startTime) / 1000f
                val fps = frame1 / duration
                val fps2 = frame2 / duration
                Log.i("Better", "fps:${fps.toInt()}")
                Log.i("Better", "fps2:${fps2.toInt()}")
                it.isEnabled = true
            }
        }
    }

    private fun frameDump() {
        //com.better.learn.framecall/com.better.learn.framecall.MainActivity#0
        val windowName = "com.better.learn.framecall/com.better.learn.framecall.MainActivity#0"
        SurfaceFlingerHelper.clearBuffer(windowName)
        val successDump = SurfaceFlingerHelper.dumpFrameLatency(windowName)
        if (successDump) {
            val frame = SurfaceFlingerHelper.getFrameRate()
            Log.e("Better", "fps3:$frame")
        } else {
            Log.e("Better", "fail sump fps3")
        }
    }

    private fun frameDraw() {
        frame2 = 0
    }

    private fun frameChoreo() {
        frame1 = 0
        Choreographer.getInstance().postFrameCallback(choCallback)
    }
}