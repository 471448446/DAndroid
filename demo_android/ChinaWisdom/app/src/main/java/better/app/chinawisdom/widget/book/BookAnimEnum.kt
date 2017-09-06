package better.app.chinawisdom.widget.book

/**
 * Created by better on 2017/9/6 14:57.
 */
enum class BookAnimEnum(val id: Int) {

    NONE(0), SLIDE(1), COVER(2);

    companion object {
        fun parseAnimation(i: Int) =
                when (i) {
                    0 -> NONE
                    1 -> SLIDE
                    else -> COVER
                }

    }
}