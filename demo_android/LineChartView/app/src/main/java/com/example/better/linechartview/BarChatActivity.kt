package com.example.better.linechartview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_bar_chat.*
import java.util.ArrayList

class BarChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chat)
        val points = ArrayList<BarChatView.BarPointInfo>()
        points.add(BarChatView.BarPointInfo("8/1", 2888f))
        points.add(BarChatView.BarPointInfo("8/2", 2888f))
        points.add(BarChatView.BarPointInfo("8/3", 1888f))
        points.add(BarChatView.BarPointInfo("8/4", 2888f))
        points.add(BarChatView.BarPointInfo("8/5", 4888f))
        points.add(BarChatView.BarPointInfo("8/6", 2000f))
        points.add(BarChatView.BarPointInfo("12/12", 2333f))

        main_barView.setPoints(points).setOnClickBarListener { index, _ ->
            Toast.makeText(BarChatActivity@ this, "click:${index + 1}", Toast.LENGTH_SHORT).show()
        }
    }
}
