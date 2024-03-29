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
            startPlugin(
                linearLayout,
                v,
                Constant.Plugin2.PATH,
                Constant.Plugin2.PART_KEY,
                Constant.Plugin2.ACTIVITY_MAIN
            )
//            startPlugin(
//                linearLayout,
//                v,
//                Constant.Plugin1.PATH,
//                Constant.Plugin1.PART_KEY,
//                Constant.Plugin1.ACTIVITY_MAIN
//            )
        }
        linearLayout.addView(textView)
        linearLayout.addView(button)
        val callServiceButton = Button(this)
        callServiceButton.text = "调用插件Service，结果打印到Log"
        callServiceButton.setOnClickListener { v ->
            val pluginManager: PluginManager = InitApplication.getPluginManager()
            val bundle = Bundle().apply {
                putString(Constant.KEY_ACTIVITY_CLASSNAME, Constant.Plugin1.SERVICE1)
                putString(Constant.KEY_PLUGIN_PART_KEY, Constant.Plugin1.PART_KEY)
                putString(Constant.KEY_PLUGIN_ZIP_PATH, Constant.Plugin1.PATH)
            }
            pluginManager.enter(
                this@MainActivity,
                Constant.FROM_ID_CALL_SERVICE,
                bundle,
                null
            )
        }
        linearLayout.addView(callServiceButton)
        setContentView(linearLayout)
    }

    private fun startPlugin(
        linearLayout: LinearLayout,
        v: View,
        path: String,
        partKey: String,
        activity: String,
    ) {
        val pluginManager: PluginManager = InitApplication.getPluginManager()
        val bundle = Bundle().apply {
//            putString(Constant.KEY_ACTIVITY_CLASSNAME, Constant.Plugin1.ACTIVITY_MAIN)
            putString(Constant.KEY_ACTIVITY_CLASSNAME, activity)
            putString(Constant.KEY_PLUGIN_PART_KEY, partKey)
            putString(Constant.KEY_PLUGIN_ZIP_PATH, path)
        }
        pluginManager.enter(
            this@MainActivity,
            Constant.FROM_ID_START_ACTIVITY,
            bundle,
            object : EnterCallback {
                override fun onShowLoadingView(view: View) {
                    this@MainActivity.setContentView(view) //显示Manager传来的Loading页面
                }

                override fun onCloseLoadingView() {
                    this@MainActivity.setContentView(linearLayout)
                    v.isEnabled = true
                }

                override fun onEnterComplete() {
                }
            })
    }
}