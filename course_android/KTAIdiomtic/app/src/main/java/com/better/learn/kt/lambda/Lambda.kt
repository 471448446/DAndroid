package com.better.learn.kt.lambda

import android.content.Context
import android.view.View
import android.widget.TextView

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
    //
    addOffset(1) { x -> x + 1 }
    addOffset({ x -> x + 1 }, 1)
    //
    addOffset(1) { it + 1 }
    addOffset({ it + 1 }, 1)
}

fun testCapturing(context: Context) {
    val view = TextView(context)
    view.setOnClickListener {
        view.text = ""
    }
}

fun testNotCapturing(context: Context) {
    val view = TextView(context)
    view.setOnClickListener {
        (it as TextView).text = it.javaClass.simpleName
    }
}

//fun createMustSAM1(): View.OnClickListener {
//    return { v: View -> }
//}

fun createMustSAM(): View.OnClickListener {
    return View.OnClickListener { }
}