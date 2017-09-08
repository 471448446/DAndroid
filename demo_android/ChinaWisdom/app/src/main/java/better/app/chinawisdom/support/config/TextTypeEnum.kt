package better.app.chinawisdom.support.config

import android.graphics.Typeface
import better.app.chinawisdom.App

/**
 * O提供了xml字体，但是由于studio2.3.3没有升级，创建不了family文件，所以用assert方式来修改字体
 * Created by better on 2017/9/7 14:34.
 */
enum class TextTypeEnum(val id: Int, private val path: String) {
    Default(0, ""),
    Italics(1, "font/Italics.ttf"),
//    Times_New_Roman(2, "font/Times_New_Roman.ttf"),
    Lanting(2, "font/Lanting.ttf"),
    Cartoon(3, "font/Cartoon.ttf"),
    Chinese_Traditional(4, "font/Chinese_Traditional.ttf");

    companion object {
        fun parseType(type: Int): TextTypeEnum = when (type) {
            1 -> Italics
            2 -> Lanting
            3 -> Cartoon
            4 -> Chinese_Traditional
            else -> Default
        }
    }

    fun createTypeFace(): Typeface = if (Default == this) {
        Typeface.DEFAULT
    } else {
        Typeface.createFromAsset(App.instance.assets, this.path)
    }
}