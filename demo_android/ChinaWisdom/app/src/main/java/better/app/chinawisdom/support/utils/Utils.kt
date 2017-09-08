package better.app.chinawisdom.support.utils

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import better.app.chinawisdom.App
import better.app.chinawisdom.R
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

fun makeSelector(): StateListDrawable = makeSelector(SettingConfig.configBgType.getColorRes())
fun makeSelector(color: Int): StateListDrawable {
    val selector = StateListDrawable()
    selector.setExitFadeDuration(400)
    selector.alpha = 45
    val select = kotlin.IntArray(1)
    select[0] = android.R.attr.state_pressed
    selector.addState(select, ColorDrawable(ContextCompat.getColor(App.instance, R.color.colorGray_select)))
    selector.addState(kotlin.IntArray(0), ColorDrawable(color))
    return selector

}

