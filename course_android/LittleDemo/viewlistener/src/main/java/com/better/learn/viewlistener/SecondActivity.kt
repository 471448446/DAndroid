package com.better.learn.viewlistener

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            onBackPressed()
        }
        return super.onKeyDown(keyCode, event)
    }
}
