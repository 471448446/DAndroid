package com.example.better.linechartview

import android.graphics.Color
import android.graphics.PointF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_battery_line.*
import java.util.ArrayList

class BatteryLineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery_line)
//        lineMuti()
        linePoint()
//        lineVertical()
    }

    private fun lineVertical() {
        val lines = ArrayList<BatteryLineView.LineInfo>()
        val lineOne2 = ArrayList<PointF>()
        lineOne2.add(PointF(12f, 55f))
        lineOne2.add(PointF(14.2f, 55f))
        lineOne2.add(PointF(14.8f, 64f))
        lineOne2.add(PointF(14.9f, 84f))
        lineOne2.add(PointF(16f, 84f))

        lines.add(BatteryLineView.LineInfo()
                .setPoints(lineOne2)
                .setCurve(LineChatView.LineInfo.LINE)
                .setLineGradientSColor(Color.parseColor("#1a0056ff"))
                .setLineGradientEColor(Color.parseColor("#000056ff"))
        )

        main_batteryLineView.setPointsList(lines)

    }

    private fun linePoint() {
        val lines = ArrayList<BatteryLineView.LineInfo>()
        val line0 = ArrayList<PointF>()
        line0.add(PointF(9f, 90f))
        val line1 = ArrayList<PointF>()
        line1.add(PointF(12f, 70f))
        val line2 = ArrayList<PointF>()
        line2.add(PointF(16f, 60f))
        val line3 = ArrayList<PointF>()
        line3.add(PointF(20f, 50f))
        line3.add(PointF(21f, 55f))
        line3.add(PointF(22f, 70f))

        val lineT = ArrayList<PointF>()
        lineT.add(PointF(0f, 100f))
        lineT.add(PointF(1f, 90f))
        lineT.add(PointF(2f, 85f))
        lineT.add(PointF(3f, 77f))

        lines.add(BatteryLineView.LineInfo()
                .setPoints(lineT)
                .setCurve(LineChatView.LineInfo.LINE)
                .setLineGradientSColor(Color.parseColor("#1a0056ff"))
                .setLineGradientEColor(Color.parseColor("#000056ff"))
        )

        lines.add(BatteryLineView.LineInfo()
                .setPoints(line0)
                .setCurve(LineChatView.LineInfo.LINE)
                .setLineGradientSColor(Color.parseColor("#1a0056ff"))
                .setLineGradientEColor(Color.parseColor("#000056ff"))
        )
        lines.add(BatteryLineView.LineInfo()
                .setPoints(line1)
                .setCurve(LineChatView.LineInfo.LINE)
                .setLineGradientSColor(Color.parseColor("#1a0056ff"))
                .setLineGradientEColor(Color.parseColor("#000056ff"))
        )
        lines.add(BatteryLineView.LineInfo()
                .setPoints(line2)
                .setCurve(LineChatView.LineInfo.LINE)
                .setLineGradientSColor(Color.parseColor("#1a0056ff"))
                .setLineGradientEColor(Color.parseColor("#000056ff"))
        )
        lines.add(BatteryLineView.LineInfo()
                .setPoints(line3)
                .setCurve(LineChatView.LineInfo.LINE)
                .setLineGradientSColor(Color.parseColor("#1a0056ff"))
                .setLineGradientEColor(Color.parseColor("#000056ff"))
        )

        main_batteryLineView.setPointsList(lines)

    }

    fun lineMuti() {
        val lines = ArrayList<BatteryLineView.LineInfo>()
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
//        lineOne.add(PointF(12.4f, 77f))
//        lineOne.add(PointF(12.6f, 76f))
//        lineOne.add(PointF(13f, 73f))
//        lineOne.add(PointF(14f, 77f))
//        lineOne.add(PointF(14.2f, 80f))
//        lineOne.add(PointF(14.8f, 84f))
//        lineOne.add(PointF(15f, 88f))
//        lineOne.add(PointF(15.2f, 90f))
//        lineOne.add(PointF(16f, 95f))
//        lineOne.add(PointF(17f, 98f))
//        lineOne.add(PointF(18f, 98.2f))
//        lineOne.add(PointF(19f, 98.8f))
//        lineOne.add(PointF(19.3f, 99f))
//        lineOne.add(PointF(19.9f, 99.8f))
//        lineOne.add(PointF(20f, 100f))
//        lineOne.add(PointF(20.3f, 100f))
//        lineOne.add(PointF(20.6f, 100f))
//        lineOne.add(PointF(21f, 100f))
//        lineOne.add(PointF(22f, 100f))

        val lineT = ArrayList<PointF>()
        lineT.add(PointF(0f, 100f))
        lineT.add(PointF(1f, 90f))
        lineT.add(PointF(2f, 85f))
        lineT.add(PointF(3f, 77f))

        val lineOne2 = ArrayList<PointF>()
        lineOne2.add(PointF(14f, 77f))
        lineOne2.add(PointF(14.2f, 80f))
        lineOne2.add(PointF(14.8f, 84f))
        lineOne2.add(PointF(15f, 88f))
        lineOne2.add(PointF(15.2f, 90f))

        lines.add(BatteryLineView.LineInfo()
                .setPoints(lineT)
                .setCurve(LineChatView.LineInfo.LINE)
                .setLineGradientSColor(Color.parseColor("#1a0056ff"))
                .setLineGradientEColor(Color.parseColor("#000056ff"))
        )
        lines.add(BatteryLineView.LineInfo()
                .setPoints(lineOne)
                .setCurve(LineChatView.LineInfo.LINE)
                .setLineGradientSColor(Color.parseColor("#1a0056ff"))
                .setLineGradientEColor(Color.parseColor("#000056ff"))
        )
        lines.add(BatteryLineView.LineInfo()
                .setPoints(lineOne2)
                .setCurve(LineChatView.LineInfo.LINE)
                .setLineGradientSColor(Color.parseColor("#1a0056ff"))
                .setLineGradientEColor(Color.parseColor("#000056ff"))
        )

        main_batteryLineView.setPointsList(lines)
    }
}
