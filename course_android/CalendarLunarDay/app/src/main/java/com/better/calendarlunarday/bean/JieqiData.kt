package com.better.calendarlunarday.bean

import com.better.calendarlunarday.utils.solarTermsName

data class JieqiData(val index: Int, val year: Int, val month: Int, val day: Int, val name: String = solarTermsName(index))