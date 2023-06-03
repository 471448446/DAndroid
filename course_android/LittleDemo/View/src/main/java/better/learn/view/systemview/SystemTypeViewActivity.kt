package better.learn.view.systemview

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import better.learn.view.BuildConfig
import better.learn.view.R

/**
 * 通过Window添加VIew，设置一个高的order
 */

class SystemTypeViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_type_view)
        findViewById<View>(R.id.view_genSysView).setOnClickListener {
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Settings.canDrawOverlays(this)
                    } else {
                        // VERSION.SDK_INT < M
                        true
                    }) {
                genView()
            } else {
                val t = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + BuildConfig.APPLICATION_ID))
                startActivityForResult(t, 10)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != 10) return
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Settings.canDrawOverlays(this)
                } else {
                    true
                }) {
            genView()
        }
    }

    fun genView() {
        val buttonView = Button(this)
        buttonView.text = "高级别View,覆盖在最上层"
        val window: WindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val parmas = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT)

        buttonView.setOnClickListener {
            Log.d("Main", "点击")
            Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show()
        }
        window.addView(buttonView, parmas)


    }
}
