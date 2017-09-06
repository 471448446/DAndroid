package better.app.chinawisdom.util.extenions

/**
 * Created by better on 2017/8/27 19:31.
 */
fun String.notEmpty(block: (s: String) -> Unit) {
    if (!isEmpty()) {
        block(this)
    }
}