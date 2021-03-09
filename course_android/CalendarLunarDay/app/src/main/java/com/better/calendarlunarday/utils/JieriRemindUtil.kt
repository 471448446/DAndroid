package com.better.calendarlunarday.utils

import com.better.calendarlunarday.bean.JieqiData
import com.better.calendarlunarday.nongliManager.CnNongLiManager
import java.lang.Exception
import java.util.*

/**
 * 获取今年的二十四节气
 * 从立春到大汗截至
 */
fun thisYear24SolarTerms(): List<JieqiData> {
    val calendar = Calendar.getInstance()
    var cYear = calendar.get(Calendar.YEAR)
    // 每年的小寒和大寒是在阳历的第二年
    // 所以如果是1月来看今年的节气，其实是看的阳历去年的节气，年份减一
    if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
        cYear--
    }
    val cnNongLiManager = CnNongLiManager()
    // 从2月开始算到明年1月
    return (1 until 13).map { index ->
        val m = index % 12 + 1
        val y = if (index < 12) cYear else cYear + 1
        val firstJieQiOfMonthIndex = (m - 1) * 2
        arrayOf(
                JieqiData(firstJieQiOfMonthIndex, y, m, cnNongLiManager.getJieqi(y, firstJieQiOfMonthIndex)),
                JieqiData(firstJieQiOfMonthIndex + 1, y, m, cnNongLiManager.getJieqi(y, firstJieQiOfMonthIndex + 1))
        )
    }.flatMap { it.toList() }
}

/**
 * 节气本地名称
 */
fun solarTermsName(index: Int): String = try {
    CnNongLiManager.jieqi[index]
} catch (e: Exception) {
    ""
}