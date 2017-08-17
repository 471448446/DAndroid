package better.hellokotlin.util

import android.util.Log

/**
 * Created by better on 2017/7/29 14:08.
 */
/**
 * 顶层定义函数
 * 以前的static函数，现在可以这样申明
 */
public fun log(tag: String, s: String) {
    Log.d(tag, s)
}

public fun log(s: String) {
    Log.d("Better", s)
}