package com.example.better.linechartview

import android.graphics.Color
import android.graphics.PointF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class LineChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        line()
        bar()
    }

    private fun bar() {

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

    fun line() {
        val lines = ArrayList<LineChatView.LineInfo>()

        val lineOne = ArrayList<PointF>()
        lineOne.add(PointF(9f, 90f))
        lineOne.add(PointF(10.1f, 93f))
        lineOne.add(PointF(10.3f, 95f))
        lineOne.add(PointF(10.9f, 92f))
        lineOne.add(PointF(11f, 89f))
        lineOne.add(PointF(11.4f, 86f))
        lineOne.add(PointF(11.7f, 82f))
        lineOne.add(PointF(12f, 79f))
        lineOne.add(PointF(12.1f, 78f))
        lineOne.add(PointF(12.4f, 77f))
        lineOne.add(PointF(12.6f, 76f))
        lineOne.add(PointF(13f, 73f))
        lineOne.add(PointF(14f, 77f))
        lineOne.add(PointF(14.2f, 80f))
        lineOne.add(PointF(14.8f, 84f))
        lineOne.add(PointF(15f, 88f))
        lineOne.add(PointF(15.2f, 90f))
        lineOne.add(PointF(16f, 95f))
        lineOne.add(PointF(17f, 98f))
        lineOne.add(PointF(18f, 98.2f))
        lineOne.add(PointF(19f, 98.8f))
        lineOne.add(PointF(19.3f, 99f))
        lineOne.add(PointF(19.9f, 99.8f))
        lineOne.add(PointF(20f, 100f))
        lineOne.add(PointF(20.3f, 100f))
        lineOne.add(PointF(20.6f, 100f))
        lineOne.add(PointF(21f, 100f))
        lineOne.add(PointF(22f, 100f))

        val lineTwo = ArrayList<PointF>()
        lineTwo.add(PointF(0f, 20f))
        lineTwo.add(PointF(0.5f, 21f))
        lineTwo.add(PointF(0.7f, 21f))
        lineTwo.add(PointF(0.9f, 21f))
        lineTwo.add(PointF(1f, 21f))
        lineTwo.add(PointF(1.5f, 21f))
        lineTwo.add(PointF(2f, 21f))
        lineTwo.add(PointF(4f, 25f))
        lineTwo.add(PointF(4.4f, 30f))
        lineTwo.add(PointF(5.6f, 35f))

        val lineThree = ArrayList<PointF>()
        for (pointF in lineOne) {
            lineThree.add(PointF(pointF.x, pointF.y - 10))
        }
        val lineFour = ArrayList<PointF>()
        for (pointF in lineThree) {
            lineFour.add(PointF(pointF.x, pointF.y - 10))
        }
        //the integer literal does not conform to the expected type int
        //0xFF11B1C5
        lines.add(LineChatView.LineInfo()
                .setPoints(lineOne)
                .setCurve(LineChatView.LineInfo.LINE)
        )
        lines.add(LineChatView.LineInfo()
                .setPoints(lineThree)
                .setCurve(LineChatView.LineInfo.CURVE)
        )
        lines.add(LineChatView.LineInfo()
                .setPoints(lineFour)
                .setCurve(LineChatView.LineInfo.CURVE)
                .setLineGradientSColor(Color.parseColor("#11B1C5"))
                .setLineGradientEColor(Color.parseColor("#DEEDFA"))
        )
        lines.add(LineChatView.LineInfo()
                .setPoints(lineTwo)
                .setCurve(LineChatView.LineInfo.LINE)
        )
        main_lineView.setPointsList(lines)
    }
}
