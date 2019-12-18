package com.better.learn.testdialogshow

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog = AlertDialog.Builder(this).create()


        dialog_show.setOnClickListener {

            Thread {
                // block main handler queue,begin anr
                try {
                    dialog.show()
                } catch (e: Exception) {
                }
            }.start()
            /*
            WindowManager: android.view.WindowLeaked: Activity com.better.learn.testdialogshow.MainActivity has leaked window DecorView@9fdd9cc[] that was originally added here
            at android.view.ViewRootImpl.<init>(ViewRootImpl.java:576)
            at android.view.WindowManagerGlobal.addView(WindowManagerGlobal.java:363)
            at android.view.WindowManagerImpl.addView(WindowManagerImpl.java:128)
            at android.app.Dialog.show(Dialog.java:454)
            at com.better.learn.testdialogshow.MainActivity$onCreate$1$1.run(MainActivity.kt:22)
             */

            it.postDelayed({
                try {
                    dialog.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, 3_000)
        }
    }
}
