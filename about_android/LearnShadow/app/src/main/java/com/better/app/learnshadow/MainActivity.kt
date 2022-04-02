package com.better.app.learnshadow

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.better.app.plugin_constant.Constant
import com.better.app.plugin_host_lib.InitApplication
import com.tencent.shadow.dynamic.host.EnterCallback
import com.tencent.shadow.dynamic.host.PluginManager

class MainActivity : AppCompatActivity() {

    val FROM_ID_START_ACTIVITY = 1001
    val FROM_ID_CALL_SERVICE = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        val textView = TextView(this)
        textView.text = "宿主App"
        val button = Button(this)
        button.text = "启动插件"
        button.setOnClickListener { v ->
            v.isEnabled = false //防止点击重入
            val pluginManager: PluginManager = InitApplication.getPluginManager()
            val bundle = Bundle().apply {
                putString(Constant.KEY_ACTIVITY_CLASSNAME, "com.better.app.plugin1.MainActivity")
                putString(Constant.KEY_PLUGIN_PART_KEY, "plugin-demo01")
                putString(Constant.KEY_PLUGIN_ZIP_PATH, "/data/local/tmp/plugin1-debug.zip")
            }
            pluginManager.enter(
                this@MainActivity,
                FROM_ID_START_ACTIVITY.toLong(),
                bundle,
                object : EnterCallback {
                    override fun onShowLoadingView(view: View) {
                        this@MainActivity.setContentView(view) //显示Manager传来的Loading页面
                    }

                    override fun onCloseLoadingView() {
                        this@MainActivity.setContentView(linearLayout)
                    }

                    override fun onEnterComplete() {
                        v.isEnabled = true
                    }
                })
        }
        linearLayout.addView(textView)
        linearLayout.addView(button)
        val callServiceButton = Button(this)
        callServiceButton.text = "调用插件Service，结果打印到Log"
        callServiceButton.setOnClickListener { v ->
            v.isEnabled = false //防止点击重入
            val pluginManager: PluginManager = InitApplication.getPluginManager()
            pluginManager.enter(this@MainActivity, FROM_ID_CALL_SERVICE.toLong(), null, null)
        }
        linearLayout.addView(callServiceButton)
        setContentView(linearLayout)
    }
}