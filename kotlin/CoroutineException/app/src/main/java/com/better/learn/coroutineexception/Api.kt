package com.better.learn.coroutineexception

import kotlinx.coroutines.delay
import java.lang.RuntimeException

class Api {
    suspend fun simpleGet(): List<String> {
        delay(1000)
        return listOf("Hello", "World")
    }

    suspend fun errorGet(): List<String> {
        delay(1000)
        throw RuntimeException()
    }
}