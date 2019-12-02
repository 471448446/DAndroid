package com.better.learn.ktaextension

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

/**
 * https://proandroiddev.com/kotlin-android-extensions-using-view-binding-the-right-way-707cd0c9e648#c1f2
 * https://antonioleiva.com/kotlin-android-extensions/
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_title.visibility = View.VISIBLE
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_content, MainFragment())
            .commit()
    }
}
