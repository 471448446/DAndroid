package com.antonioleiva.weatherapp.extensions

import java.text.DateFormat
import java.util.*

fun Long.toDateString(dateFormat: Int = DateFormat.DEFAULT): String {
    val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
    return df.format(this * 1000)
}
