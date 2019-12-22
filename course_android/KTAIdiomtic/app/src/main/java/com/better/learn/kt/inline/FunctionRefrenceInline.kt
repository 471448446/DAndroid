package com.better.learn.kt.inline

/**
 * Created by better on 2019-12-21 21:51.
 */

inline fun inlinedFilter2(list : List<Int>, predicate : (Int) -> Boolean) : List<Int>{
    return list.filter(predicate)
}

fun filterLessThanTwo(input: Int) = input < 2

fun functionReferenceTest() {
    val list = listOf(1,2,3)
    val newList = inlinedFilter2(list, ::filterLessThanTwo)
    println(newList)
}