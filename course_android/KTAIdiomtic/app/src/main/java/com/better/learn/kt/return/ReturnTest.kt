package com.better.learn.kt.`return`

/**
 * Created by better on 2019-12-22 21:08.
 */

fun higherOrderFunction(lambda: () -> Unit) {
    lambda()
}

fun higherOrderFunctionTest() {
    System.out.println("higherOrderFunctionTest 1")
    higherOrderFunction label@{
        System.out.println("higherOrderFunctionTest 1 lambda try end")
        return@label
    }
    System.out.println("higherOrderFunctionTest 1 end")
    System.out.println("higherOrderFunctionTest 2")
    higherOrderFunction {
        System.out.println("higherOrderFunctionTest 2 lambda try end")
        return@higherOrderFunction
    }
    System.out.println("higherOrderFunctionTest 1 end")

}

fun anonymousTest() {
    System.out.println("anonymousTest 1")
    higherOrderFunction(fun() {
        System.out.println("anonymousTest  anonymous try end")
        return
    })
    System.out.println("anonymousTest end")
}

inline fun higherOrderFunction1(lambda: () -> Unit) {
    lambda()
}

fun higherOrderFunction1Test() {
    System.out.println("higherOrderFunction1Test 1")
    higherOrderFunction(fun() {
        System.out.println("higherOrderFunction1Test  inline try end")
        return
    })
    System.out.println("higherOrderFunction1Test end")
}


fun main() {
    higherOrderFunctionTest()
    anonymousTest()
}