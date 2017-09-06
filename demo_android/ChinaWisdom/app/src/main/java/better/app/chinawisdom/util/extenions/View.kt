package better.app.chinawisdom.util.extenions

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
