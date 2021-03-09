package com.better.calendarlunarday

import com.better.calendarlunarday.nongliManager.CnNongLiManager
import com.better.calendarlunarday.utils.AlmanacCommentUtils
import com.better.calendarlunarday.utils.AlmanacZiTi
import com.better.calendarlunarday.utils.thisYear24SolarTerms
import org.junit.Before
import org.junit.Test
import java.util.*

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
        val tripleToday = Calendar.getInstance().run {
            Triple(get(Calendar.YEAR), get(Calendar.MONTH) + 1, get(Calendar.DAY_OF_MONTH))
        }
        val cnNongLiManager = CnNongLiManager()
        val nongli = cnNongLiManager.calGongliToNongli(tripleToday.first, tripleToday.second, tripleToday.third)
        println(nongli.joinToString(","))
    }

    @Test
    fun calDayTimeCyclicalTest() {
        // 某天时辰吉凶、冲煞
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

    @Test
    fun tesThisYearJieqi() {
        println(
                thisYear24SolarTerms().joinToString("\n")
        )
    }

    /**
     * TimeJiXiongActivity
     * 推演某天某个时辰的吉凶
     */
    @Test
    fun todayShiChengJiXiong() {
        val str_shichen_array = arrayOf(
                "23:00-00:59",
                "01:00-02:59",
                "03:00-04:59",
                "05:00-06:59",
                "07:00-08:59",
                "09:00-10:59",
                "11:00-12:59",
                "13:00-14:59",
                "15:00-16:59",
                "17:00-18:59",
                "19:00-20:59",
                "21:00-22:59"
        )
        val tripleToday = Calendar.getInstance().run {
            Triple(get(Calendar.YEAR), get(Calendar.MONTH) + 1, get(Calendar.DAY_OF_MONTH))
        }
        println("${tripleToday.first}/${tripleToday.second}/${tripleToday.third}")
        val calDayTimeCyclical = cnNongLiManager.calDayTimeCyclical(
                tripleToday.first,
                tripleToday.second,
                tripleToday.third
        )
        val nongli = cnNongLiManager.calGongliToNongli(
                tripleToday.first,
                tripleToday.second,
                tripleToday.third
        )
        println(
                nongli.joinToString(",")
        )
        val yiji: (Int, Int) -> String = { shicheng: Int, gz: Int ->
//            数据库读取宜忌
            val sql = "select * from huanglishichen where sc=$shicheng and gz=${gz % 60}"
            sql
        }
        println(
                calDayTimeCyclical.mapIndexed { index, ints ->
                    arrayOf(
                            // 时间
                            str_shichen_array[index],
                            // 12时辰
                            AlmanacZiTi.f[ints[0] % 10] + AlmanacZiTi.g[ints[0] % 12] + "",
                            // 吉凶
                            (if (ints[1] == 0) "凶" else "吉"),
                            // 冲煞
                            cnNongLiManager.calChongSha(ints[0]),
                            // 五神方位
                            AlmanacCommentUtils.getInstance().wuShenFangwei(ints[0]),
                            // 宜忌
                            yiji(index, nongli[5].toInt())
                    ).joinToString(",")
                }.joinToString("\n")
        )
    }
}