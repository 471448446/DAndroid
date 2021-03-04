package com.better.calendarlunarday

import com.better.calendarlunarday.nongliManager.CnNongLiManager
import org.junit.Before
import org.junit.Test

class CnNongLiManagerTest2 {
    companion object {
        const val TAG = "Lunar"
    }

    lateinit var cnNongLiManager: CnNongLiManager

    @Before
    fun before() {
        cnNongLiManager = CnNongLiManager()
    }

    @Test
    fun calGongliToNongli() {
        val cnNongLiManager = CnNongLiManager()
        val nongli = cnNongLiManager.calGongliToNongli(2021, 3, 4)
        println(nongli.joinToString(","))
    }

    @Test
    fun calDayTimeCyclicalTest() {
        //TimeJiXiongActivity 某天时辰吉凶、冲煞
        val calDayTimeCyclical = cnNongLiManager.calDayTimeCyclical(2021, 3, 4)
        println(
            calDayTimeCyclical.mapIndexed { index, ints ->
                "\nindex_$index:" + ints.joinToString(",") +
                        // 说人话
                        "翻译: " +
                        (if (ints[1] == 0) "凶" else "吉") + "," + cnNongLiManager.calChongSha(ints[0])
            }
        )
    }
}