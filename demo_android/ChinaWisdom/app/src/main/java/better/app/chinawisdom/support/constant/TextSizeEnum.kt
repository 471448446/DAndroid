package better.app.chinawisdom.support.constant

/**
 * Created by better on 2017/9/8 14:37.
 */
enum class TextSizeEnum(val testSize: Float) {
    Small(14f), Normal(20f), Big(24f), Huge(28f), BigHug(32f);

    companion object {
        fun parse(index: Int): TextSizeEnum = when (index) {
            0 -> Normal
            2 -> Big
            3 -> Huge
            4 -> BigHug
            else -> Normal
        }
    }
}