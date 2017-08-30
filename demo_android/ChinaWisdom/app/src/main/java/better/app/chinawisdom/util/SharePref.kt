package better.app.chinawisdom.util

import android.preference.PreferenceManager
import better.app.chinawisdom.App
import better.app.chinawisdom.extensions.get
import better.app.chinawisdom.extensions.put

/**
 * Created by better on 2017/8/18 16:58.
 */
object SharePref {
    val KEY_INIT_BOOK = "key_init_book"
    val KEY_BOOK_SELECT = "key_book_select"
    val KEY_BOOK_CHAPTER_SELECT = "key_book_chapter_select"
    val KEY_BOOK_CHAPTER_SELECT_NAME = "key_book_chapter_select_name"
    val KEY_BOOK_CHAPTER_SELECT_READ = "key_book_chapter_select_read"
    fun put(key: String, value: Any) {
        val p = PreferenceManager.getDefaultSharedPreferences(App.instance)
        p.put(key, value)
    }

    fun getBoolean(key: String): Boolean =
            PreferenceManager.getDefaultSharedPreferences(App.instance).getBoolean(key, false)

    fun getInt(key: String): Int =
            PreferenceManager.getDefaultSharedPreferences(App.instance).getInt(key, 0)

    fun getString(key: String): String =
            PreferenceManager.getDefaultSharedPreferences(App.instance).getString(key, "")

    /**
     * 不行？
     */
    inline fun <reified T : Any> get(key: String): T {
        log("__" + T::class.java.simpleName)
        return PreferenceManager.getDefaultSharedPreferences(App.instance).get<T>(key)
    }


}