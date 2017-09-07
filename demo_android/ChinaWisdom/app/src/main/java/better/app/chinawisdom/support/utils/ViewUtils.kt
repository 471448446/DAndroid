package better.app.chinawisdom.support.utils

import android.view.View
import better.app.chinawisdom.App

/**
 * Created by better on 2017/8/19 16:19.
 */
object ViewUtils {
    fun visibleView(vararg views: View) {
        views.forEach { it.visibility = View.VISIBLE }
    }

    fun inVisibleView(vararg views: View) {
        views.forEach { it.visibility = View.INVISIBLE }
    }

    fun goneView(vararg views: View) {
        views.forEach { it.visibility = View.GONE }
    }

    fun px2sp(pxValue: Float): Float {
        val fontScale = App.instance.getResources().getDisplayMetrics().scaledDensity
        return pxValue / fontScale + 0.5f
    }

    fun dip2px(dipValue: Float): Float {
        val scale = App.instance.getResources().getDisplayMetrics().density
        return dipValue * scale + 0.5f
    }


}
