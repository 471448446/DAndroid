package better.app.chinawisdom.widget.book

/**
 * Created by better on 2017/8/25 14:41.
 */
data class BookPage(val begin: Int, val end: Int, val lines: List<String>) {
    override fun toString(): String {
        return "b=$begin,end=$end，${lines}"
    }
}