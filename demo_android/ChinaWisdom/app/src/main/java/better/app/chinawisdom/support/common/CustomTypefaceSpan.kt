package better.app.chinawisdom.support.common

import android.graphics.Typeface
import android.preference.Preference
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.TypefaceSpan

/**
 * https://stackoverflow.com/questions/13193405/set-custom-font-for-text-in-preferencescreen
 * Created by better on 2017/9/7 21:51.
 */
class CustomTypefaceSpan(/*private val customFamily: String,*/ private val face: Typeface) : TypefaceSpan("") {
    companion object {
        fun convertPreferenceToUseCustomFont(tf: Typeface, vararg pres: Preference) {
            pres.forEach {
                convertPreferenceToUseCustomFont(it, tf)
            }
        }

        fun convertPreferenceToUseCustomFont(pre: Preference, tf: Typeface) {
            val custom = CustomTypefaceSpan(tf)
            var ss: SpannableStringBuilder

            if (null != pre.title) {
                ss = SpannableStringBuilder(pre.title.toString())
                ss.setSpan(custom, 0, ss.length, SpannableStringBuilder.SPAN_EXCLUSIVE_INCLUSIVE)
                pre.title = ss
            }
            // not work？？？
            if (null != pre.summary) {
                ss = SpannableStringBuilder(pre.summary.toString())
                ss.setSpan(custom, 0, ss.length, SpannableStringBuilder.SPAN_EXCLUSIVE_INCLUSIVE)
                pre.summary = ss
            }
        }
    }

    override fun updateDrawState(ds: TextPaint?) {
        super.updateDrawState(ds)
        applyCustomTypeFace(ds, face)
    }

    override fun updateMeasureState(paint: TextPaint?) {
        super.updateMeasureState(paint)
        applyCustomTypeFace(paint, face)

    }

    private fun applyCustomTypeFace(paint: TextPaint?, tf: Typeface) {
        if (null == paint) return
        val oldStyle: Int
        val old = paint.typeface
        oldStyle = if (old == null) {
            0
        } else {
            old!!.style
        }

        val fake = oldStyle and tf.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }

        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }

        paint.typeface = tf
    }

}