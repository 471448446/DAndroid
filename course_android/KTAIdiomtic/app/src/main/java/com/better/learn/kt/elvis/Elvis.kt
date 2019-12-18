package com.better.learn.kt.elvis

class User(var name: String)

fun test(user: User?) {
    val name = user?.name ?: "default"
}