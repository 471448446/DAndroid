package better.learn.proccess.demo

import android.util.Log
import java.lang.StringBuilder

fun log(s: String) {
    Log.d("Better", s)
}

fun log(vararg s: Any) {
    val builder = StringBuilder()
    for ((index, item) in s.withIndex()) {
        builder.append("\n")
        builder.append(item.toString())
    }
    Log.d("Better", builder.toString())
}