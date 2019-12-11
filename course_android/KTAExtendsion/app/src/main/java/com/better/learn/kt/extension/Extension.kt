package com.better.learn.kt.extension

import android.util.Log

fun String.hello() {
    System.out.println("hello world")
}

val String.hello: String
    get() = "hello world"