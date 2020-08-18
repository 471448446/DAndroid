package com.better.learn.scopedstorage

inline fun catch(block: () -> Unit) {
    try {
        block()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

inline fun default(block: () -> Any?) = default(null, block)

inline fun <T> default(s: T, block: () -> T): T = try {
    block()
} catch (e: Throwable) {
    e.printStackTrace()
    s
}