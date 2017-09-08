package better.app.chinawisdom.support.config

import android.support.v4.content.ContextCompat
import better.app.chinawisdom.App
import better.app.chinawisdom.R

/**
 * Created by better on 2017/9/8 14:37.
 */
enum class ColorBgEnum(val color: Int) {
    BG0(R.color.read_bg_default), BG1(R.color.read_bg_1),
    BG2(R.color.read_bg_2), BG3(R.color.read_bg_3),
    BG4(R.color.read_bg_4);

    companion object {
        fun parseType(type: Int): ColorBgEnum = when (type) {
            1 -> BG1
            2 -> BG2
            3 -> BG3
            4 -> BG4
            else -> BG0
        }
    }

    fun getColorRes(): Int =
            ContextCompat.getColor(App.instance, color)
}