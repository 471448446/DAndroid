package better.app.chinawisdom.support.utils

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager

/**
 * Created by better on 2017/11/28 09:33.
 */
object SystemBarHelper {
    /**
     * 真实的color值
     */
    fun setBarColor(activity: Activity, color: Int) {
        if (canNotChange()) {
            return
        }
        var window =activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility =View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor =color
    }

    private fun canNotChange() = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
}