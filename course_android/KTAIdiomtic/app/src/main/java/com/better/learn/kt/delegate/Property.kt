package com.better.learn.kt.delegate

import kotlin.properties.Delegates

var name: String? = null
    set(value) {
        field = value
    }
    get() {
        return field
    }
const val DAVID = "david"

val name1: String by lazy { "god" }

lateinit var name2: String

var name3: String by Delegates.notNull()

var name4: String by Delegates.observable("<Initial Value>") { property, oldValue, newValue ->
    println("Property ${property.name} changed value from $oldValue to $newValue")
}
var name5: Int by Delegates.vetoable(0) { property, oldValue, newValue ->
    println("${property.name} $oldValue -> $newValue")
    newValue % 2 == 0
}