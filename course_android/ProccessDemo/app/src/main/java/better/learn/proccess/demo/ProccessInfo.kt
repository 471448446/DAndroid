package better.learn.proccess.demo

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.content.Context.ACTIVITY_SERVICE


fun showProcess(ctx: Context) {
    val activityManager: ActivityManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val list: List<ActivityManager.RunningAppProcessInfo> = activityManager.runningAppProcesses

    for (item in list) {
        log(Process.myPid(), item.processName, item.pid, item.uid)
    }
}

fun showMemory(ctx: Context) {
    val activityManager = ctx.getSystemService(ACTIVITY_SERVICE) as ActivityManager
    val info = ActivityManager.MemoryInfo()
    activityManager.getMemoryInfo(info)

    log("系统总内存:" + (info.totalMem shr 20) + "MB")
    log("系统剩余内存:" + (info.availMem shr 20) + "MB")
    log("系统是否处于低内存运行：" + info.lowMemory)
    log("当系统剩余内存低于" + (info.threshold shr 20) + "MB时就看成低内存运行")

    val rt = Runtime.getRuntime()

    log("Available heap " + (rt.freeMemory() shr 20) + "MB")
    log("MAX heap " + (rt.maxMemory() shr 20) + "MB")
    log("total heap " + (rt.totalMemory() shr 20) + "MB")
}