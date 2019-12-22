package com.better.learn.kt.highorder

/**
 * Created by better on 2019-12-21 20:23.
 */

val lambda1: (Int) -> Boolean = { it > 2 }
val lambda2 = { x: Int -> x > 2 }

val anonymous1 = fun(x: Int): Boolean { return x > 2 }
val anonymous2 = fun(x: Int) = x > 2

val functionReference = String::filter

fun main() {
    lambda1(1)
    lambda2.invoke(1)
    anonymous1(1)
    anonymous2.invoke(1)
    functionReference.invoke("") { true }
}