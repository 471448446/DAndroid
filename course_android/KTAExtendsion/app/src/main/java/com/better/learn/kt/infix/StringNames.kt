package com.better.learn.kt.infix

class StringNames {
    infix fun appendOther(other: String): String {
        return "hello $other"
    }
}