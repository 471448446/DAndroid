package com.better.learn.kt.sealed

sealed class NaviBar(private val name: String) {
    class RedNaviBar : NaviBar("red")
}

class GreedNaviBar : NaviBar("greed")

public fun transNaviBar(naviBar: NaviBar): String {
    return when (naviBar) {
        is NaviBar.RedNaviBar -> {
            "is Red"
        }
        else -> {
            "else"
        }
    }
}