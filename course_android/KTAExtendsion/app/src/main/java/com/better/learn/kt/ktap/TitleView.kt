package com.better.learn.kt.ktap

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.better.learn.kt.R
import kotlinx.android.synthetic.main.view_title.view.*

class TitleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_title, this, true)
        view_title.text = "Background KTA Extension"
    }
}
