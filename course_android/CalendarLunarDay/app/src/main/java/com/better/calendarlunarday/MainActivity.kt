package com.better.calendarlunarday

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.better.calendarlunarday.nongliManager.CnNongLiManager
import com.better.calendarlunarday.utils.AlmanacZiTi
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.test_text)
        test1(textView)

    }

    private fun test1(textView: TextView) {
        textView.typeface = Typeface.createFromAsset(this.resources.assets, "iconfont.ttf")
        val tripleToday = Calendar.getInstance().run {
            Triple(get(Calendar.YEAR), get(Calendar.MONTH) + 1, get(Calendar.DAY_OF_MONTH))
        }
        val text = CnNongLiManager().calDayTimeCyclical(
            tripleToday.first,
            tripleToday.second,
            tripleToday.third
        ).run {
            val ints = get(0)
            AlmanacZiTi.f[ints[0] % 10] + AlmanacZiTi.g[ints[0] % 12] + ""
        }
        textView.text = text
        val textAll = AlmanacZiTi::class.java.declaredFields.map {
            it.name + ": " + (it.get(null) as? Array<*>)?.joinToString(",")
        }.joinToString("\n")
        textView.text = textAll
    }
}