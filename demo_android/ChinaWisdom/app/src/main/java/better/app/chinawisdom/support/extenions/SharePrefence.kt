package better.app.chinawisdom.support.extenions

import android.content.SharedPreferences
import better.app.chinawisdom.support.utils.log

/**
 * Created by better on 2017/8/18 16:52.
 */
fun SharedPreferences.put(key: String, value: Any) {
    val editor = edit()
    when (value) {
        is String -> editor.putString(key, value)
        is Int -> editor.putInt(key, value)
        is Long -> editor.putLong(key, value)
        is Boolean -> editor.putBoolean(key, value)
        is Float -> editor.putFloat(key, value)
    }
    editor.commit()
}

/**
 * First of all: JVM removes most of generic information and so does Kotlin.
 * But Kotlin has something called reified generics.
 * https://discuss.kotlinlang.org/t/checking-type-in-generic/3100
 *
 * inline fun <reified T> TreeNode.findParentOfType(): T? {
 * https://kotlinlang.org/docs/reference/inline-functions.html#reified-type-parameters
 *
 */
inline fun <reified T : Any> SharedPreferences.get(key: String): T {
    log("__" + T::class.java.simpleName)

    when (T::class) {
        String::class -> {
            return getString(key, "") as T
        }
        Int::class -> {
            return getInt(key, -1) as T
        }
        Long::class -> {
            return getLong(key, -1) as T
        }
        Boolean::class -> {
            return getBoolean(key, false) as T
        }
        Float::class -> {
            return getFloat(key, -0f) as T
        }
        else -> throw Exception("SharedPreferences 类型")
    }
}
