package com.better.learn.kt.inline

/**
 * Created by better on 2019-12-21 21:27.
 */

fun notInlinedFilter(list: List<Int>, predicate: (Int) -> Boolean): List<Int> {
    return list.filter(predicate)
}

fun notInlinedTest() {
    val list = listOf(1, 2, 3)
    val newList = notInlinedFilter(list) { it < 2 }
    println(newList)
}