package com.better.learn.matrix

import android.app.Application
import com.better.learn.matrix.matrix.DynamicConfigImplDemo
import com.better.learn.matrix.matrix.TestPluginListener
import com.tencent.matrix.Matrix
import com.tencent.matrix.iocanary.IOCanaryPlugin
import com.tencent.matrix.iocanary.config.IOConfig
import com.tencent.matrix.trace.TracePlugin
import com.tencent.matrix.trace.config.TraceConfig

class App : Application() {
    companion object {
        lateinit var shared: App
    }

    override fun onCreate() {
        super.onCreate()
        shared = this
        initMatrix()
    }

    private fun initMatrix() {
        val dynamicConfig = DynamicConfigImplDemo()
        val fpsEnable: Boolean = dynamicConfig.isFPSEnable
        val traceEnable: Boolean = dynamicConfig.isTraceEnable

        val builder: Matrix.Builder = Matrix.Builder(this) // build matrix
        builder.patchListener(TestPluginListener(this)) // add general pluginListener

        //ioCanary Plugin
        val ioCanaryPlugin = IOCanaryPlugin(
            IOConfig.Builder()
                .dynamicConfig(dynamicConfig)
                .build()
        )
        //add to matrix
        builder.plugin(ioCanaryPlugin)

        //trace Plugin
        val traceConfig = TraceConfig.Builder()
            .dynamicConfig(dynamicConfig)
            .enableFPS(fpsEnable)
            .enableEvilMethodTrace(traceEnable)
            .enableAnrTrace(traceEnable)
            .enableStartup(traceEnable)
            .splashActivities("com.better.learn.matrix.SplashActivity;")
            .isDebug(true)
            .isDevEnv(false)
            .build()
        val tracePlugin = TracePlugin(traceConfig)
        builder.plugin(tracePlugin)

        //init matrix
        Matrix.init(builder.build())

        // start plugin
        ioCanaryPlugin.start()
        tracePlugin.start()
    }
}