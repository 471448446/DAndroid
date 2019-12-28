package com.better.learn.testdialogshow

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var dialog: Dialog
    lateinit var view: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog = AlertDialog.Builder(this)
            .setTitle(R.string.app_name)
            .create()

        anr()

//        directAddViewInThread()

        blockMain()
    }

    private fun blockMain() {
        // block main 之后，传输输入时间查看ANR
        dialog_show.setOnClickListener {
            try {
                Log.e("ANR", "try block main Thread: " + Thread.currentThread().name)
                Thread.sleep(10_000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun directAddViewInThread() {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        view = TextView(this).apply {
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
        }
        dialog_show.setOnClickListener {
            Log.e("ANR", "try add view to window (Thread)")
            // 虽然没ANR，但是没有显示View
            Thread {
                try {
                    windowManager.addView(
                        view,
                        WindowManager.LayoutParams().apply {
                            width = 200
                            height = 300
                            gravity = Gravity.CENTER
                            type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL
                            flags =
                                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            format = PixelFormat.TRANSLUCENT
                        }
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                // onBackPress print
                /*
                WindowManager: android.view.WindowLeaked: Activity com.better.learn.testdialogshow.MainActivity has leaked window android.widget.TextView{d723e70 V.ED..... ......ID 0,0-0,0} that was originally added here
        at android.view.ViewRootImpl.<init>(ViewRootImpl.java:576)
        at android.view.WindowManagerGlobal.addView(WindowManagerGlobal.java:363)
        at android.view.WindowManagerImpl.addView(WindowManagerImpl.java:128)
        at com.better.learn.testdialogshow.MainActivity$onCreate$1$1.run(MainActivity.kt:33)
                 */
            }.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::view.isInitialized) {
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.removeView(view)
        }
    }

    private fun anr() {
        dialog_show.setOnClickListener {
            Log.e("ANR", "try show dialog (Thread)")

            Thread {
                // block main handler queue,begin anr
                try {
                    dialog.show()
                } catch (e: Exception) {
                    // 并没有打印异常信息
                    /*
                     1. 如果访问网络，应该是：NetworkOnMainThreadException
                     2. 如果是 访问view应该是：CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its view
                     但是dialog show() 处并没有 checkThread(),dismiss()倒是checkThread()了
                     */
                    e.printStackTrace()
                }
                // 看起来是HwWindowManagerService 在添加dialog 类型的view 展示的时候，由于是非主线程，所以block了UI
                /*
                addWindow: New client android.view.ViewRootImpl$W@ae34f15: window=Window{41f422a u0 Application Not Responding: com.better.learn.testdialogshow} Callers=com.android.server.wm.HwWindowManagerService.addWindow:585 com.android.server.wm.Session.addToDisplay:199 android.view.ViewRootImpl.setView:845 android.view.WindowManagerGlobal.addView:372 android.view.WindowManagerImpl.addView:128
                 */
            }.start()


            /*
            WindowManager: android.view.WindowLeaked: Activity com.better.learn.testdialogshow.MainActivity has leaked window DecorView@9fdd9cc[] that was originally added here
            at android.view.ViewRootImpl.<init>(ViewRootImpl.java:576)
            at android.view.WindowManagerGlobal.addView(WindowManagerGlobal.java:363)
            at android.view.WindowManagerImpl.addView(WindowManagerImpl.java:128)
            at android.app.Dialog.show(Dialog.java:454)
            at com.better.learn.testdialogshow.MainActivity$onCreate$1$1.run(MainActivity.kt:22)
             */
            //
            //            it.postDelayed({
            //                try {
            //                    dialog.dismiss()
            //                } catch (e: Exception) {
            //                    e.printStackTrace()
            //                }
            //            }, 3_000)
        }
    }
}
