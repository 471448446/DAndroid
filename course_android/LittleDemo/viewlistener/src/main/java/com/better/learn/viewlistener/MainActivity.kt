package com.better.learn.viewlistener

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import better.library.utils.ForWordUtil
import better.library.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView.setOnClickListener {
            ForWordUtil.to(this, SecondActivity::class.java)
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        Utils.log(Tag, "dispatchKeyEvent()", event?.action, getNameOfKeyEvent(event))
        return super.dispatchKeyEvent(event)
//        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Utils.log(Tag, "onKeyDown()", event?.action, getNameOfKeyEvent(event))
        return super.onKeyDown(keyCode, event)
//        return true
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        Utils.log(Tag, "onKeyUp()", event?.action, getNameOfKeyEvent(event))
        return super.onKeyUp(keyCode, event)
//        return true
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        Utils.log(Tag, "onKeyLongPress()", event?.action, getNameOfKeyEvent(event))
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onBackPressed() {
        Utils.log(Tag, "onBackPressed()")
        super.onBackPressed()
    }

    override fun onUserInteraction() {
        Utils.log(Tag, "__onUserInteraction()")
        super.onUserInteraction()
    }

    override fun onUserLeaveHint() {
        Utils.log(Tag, "++onUserLeaveHint()")
        super.onUserLeaveHint()
    }

    private fun getNameOfKeyEvent(event: KeyEvent?): String {
        val action = when (event?.action) {
            KeyEvent.ACTION_DOWN -> "ACTION_DOWN"
            KeyEvent.ACTION_UP -> "ACTION_UP"
            KeyEvent.ACTION_MULTIPLE -> "ACTION_MULTIPLE"
            else -> event?.action.toString()
        }
        val keyCode = when (event?.keyCode) {
            KeyEvent.KEYCODE_BACK -> "KEYCODE_BACK"
            KeyEvent.KEYCODE_HOME -> "KEYCODE_HOME"
            KeyEvent.KEYCODE_MENU -> "KEYCODE_MENU"
            KeyEvent.KEYCODE_VOLUME_UP -> "KEYCODE_VOLUME_UP"
            KeyEvent.KEYCODE_VOLUME_DOWN -> "KEYCODE_VOLUME_DOWN"
            KeyEvent.KEYCODE_VOLUME_MUTE -> "KEYCODE_VOLUME_MUTE"
            KeyEvent.KEYCODE_APP_SWITCH -> "KEYCODE_APP_SWITCH"
            KeyEvent.KEYCODE_ALL_APPS -> "KEYCODE_ALL_APPS"
            else -> event?.action.toString()
        }

        return "$action _ $keyCode"
    }

    companion object {
        const val Tag = "DispatchEvent"
    }
}
