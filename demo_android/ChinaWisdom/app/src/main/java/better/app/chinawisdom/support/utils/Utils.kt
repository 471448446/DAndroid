package better.app.chinawisdom.support.utils

import android.util.Log
import android.widget.Toast
import better.app.chinawisdom.App
import better.app.chinawisdom.SettingConfig

/**
 * Created by better on 2017/8/18 14:20.
 */

fun log(msg: String) {
    log("Better", msg)
}

fun log(tag: String, msg: String) {
    if (SettingConfig.isLog)
        Log.d(tag, msg)
}

fun toastShort(msg: String) {
    Toast.makeText(App.instance, msg, Toast.LENGTH_SHORT).show()
}

