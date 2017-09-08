package better.app.chinawisdom.support.extenions

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View

/**
 * Created by better on 2017/9/6 14:34.
 */
fun View.isVisiable() = visibility == View.VISIBLE

fun View.visiable() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.inVisable() {
    visibility = View.INVISIBLE
}

fun View.setItBackGroundDrawable(d: Drawable) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        background = d
    } else {
        setBackgroundDrawable(d)
    }
}