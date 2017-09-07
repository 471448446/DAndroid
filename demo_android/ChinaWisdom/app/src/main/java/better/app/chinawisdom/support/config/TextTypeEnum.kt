package better.app.chinawisdom.support.config

import android.graphics.Typeface
import better.app.chinawisdom.App

/**
 * Created by better on 2017/9/7 14:34.
 */
enum class TextTypeEnum(val id: Int, private val path: String) {
    Default(0, ""),
    Italics(1, "font/Italics.ttf"),
    Times_New_Roman(2, "font/Italics.ttf"),
    Lanting(3, "font/Italics.ttf"),
    Cartoon(4, "font/Cartoon.ttf"),
    Chinese_Traditional(5, "font/Chinese_Traditional.ttf");

    companion object {
        fun parseType(type: Int): TextTypeEnum = when (type) {
            1 -> Italics
            2 -> Times_New_Roman
            3 -> Lanting
            4 -> Cartoon
            5 -> Chinese_Traditional
            else -> Default
        }
    }

    fun createTypeFace(): Typeface = if (Default == this) {
        Typeface.DEFAULT
    } else {
        Typeface.createFromAsset(App.instance.assets, this.path)
    }
}