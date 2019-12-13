package com.better.learn.kt.localfunc

fun login(username: String, password: String) : Boolean {
    var something = 1
    fun validateInput(input: String){
        something++
        if (input.isEmpty()) {
            throw IllegalArgumentException("Must not be empty")
        }
    }
    validateInput(username)
    validateInput(password)
    return true
}