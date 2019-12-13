package com.better.learn.kt.lambda

val addOffset = { x: Int -> x + 1 }

fun addOffset(init: Int, block: (Int) -> Int): Int = block(init)

fun addOffset(block: (Int) -> Int, init: Int): Int = block(init)

fun main() {
    println(addOffset(1))
    println(addOffset.invoke(1))
    println({ x: Int -> x + 1 }(1))
    // pass
    addOffset(1) { x: Int -> x + 1 }
    addOffset({ x: Int -> x + 1 }, 1)
}