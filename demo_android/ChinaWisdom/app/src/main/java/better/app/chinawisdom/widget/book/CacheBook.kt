package better.app.chinawisdom.widget.book

import java.lang.ref.WeakReference

/**
 * Created by better on 2017/8/25 14:48.
 */
data class CacheBook(val size: Int, val data: WeakReference<CharArray>)