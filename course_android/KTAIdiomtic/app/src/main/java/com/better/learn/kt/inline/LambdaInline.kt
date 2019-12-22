package com.better.learn.kt.inline

/**
 * Created by better on 2019-12-21 21:34.
 */

inline fun inlinedFilter(list : List<Int>, predicate : (Int) -> Boolean) : List<Int>{
    return list.filter(predicate)
}

fun lambdaInCallSiteTest() {
    val list = listOf(1,2,3)
    val newList = inlinedFilter(list) {it < 2}
    println(newList)
}