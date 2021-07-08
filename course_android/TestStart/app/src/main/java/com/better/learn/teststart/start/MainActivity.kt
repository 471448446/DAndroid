package com.better.learn.teststart.start

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.os.Bundle
import android.os.Process
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.better.learn.teststart.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class MainActivity : AppCompatActivity() {
    companion object {
        fun printPermission(context: Context) {
            val permissions: Array<PermissionInfo> =
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_PERMISSIONS
                ).permissions
            for (permission in permissions) {
                Log.e("Better", "manifest permission:" + permission.name)
            }
        }
    }

    private val targetIntent: Intent by lazy {
        Intent(this, TargetActivity::class.java).also {
            it.putExtra(TargetActivity.TAG, "hello")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TargetActivity.TAG, "onCreate: ")
        setContentView(R.layout.activity_main)
//        startActivity(targetIntent)
//        finish()
        button_alarm_start.setOnClickListener {
            alarmStart(targetIntent)
        }
        val preferences = getPreferences(Context.MODE_PRIVATE)
        preferences.edit()
            .apply {
                putString("A", "B")
            }
            .apply()
        val sp = File(
            filesDir.parent,
            "shared_prefs/MainActivity.xml"
        )
        TestPing().test(this, sp.absolutePath)

        Log.e(TargetActivity.TAG, "getDir(TmpDir) :" + getDir("TmpDir", 0).absolutePath)
        button_cmdline.setOnClickListener {
            Log.e(TargetActivity.TAG, a() ?: "fail /proc/pid/cmdline")
        }
        Log.e(
            "Better",
            "main check permission ${
                ActivityCompat.checkSelfPermission(
                    this,
                    "com.better.fucker"
                ) == PackageManager.PERMISSION_GRANTED
            }"
        )
        printPermission(this)
        startService(Intent(this, OtherService::class.java))
        button_cmdline.postDelayed({
            OtherService.send(applicationContext)
        }, 2000)

    }

    private fun a(): String? {
        return try {
//            获取的是包名
            val cmd = "/proc/${Process.myPid()}/cmdline"
            val v0 = BufferedReader(FileReader(File(cmd)))
            val v1: String = v0.readLine().trim()
            v0.close()
            v1
        } catch (unused_ex: Exception) {
            null
        }
    }

    private fun alarmStart(targetIntent: Intent) {
        (getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.let { alarmManager ->
            val pendingIntent = PendingIntent.getActivity(
                this,
                2020,
                targetIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(
                    AlarmManager.RTC,
                    System.currentTimeMillis() + 200,
                    pendingIntent
                )
            }
            targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            targetIntent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            startActivity(targetIntent)
        }
    }
}