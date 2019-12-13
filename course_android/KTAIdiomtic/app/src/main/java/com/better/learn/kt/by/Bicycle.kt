package com.better.learn.kt.by

interface Bicycle {
    fun bar(): String
    fun wheel(): String
}

class MountainBicycle : Bicycle {
    override fun bar(): String = "直杆"

    override fun wheel(): String = "防爆豪华轮胎"
}

class MyBiycle : Bicycle by MountainBicycle()

class AdamBicycle : Bicycle by MountainBicycle() {
    override fun bar(): String = "Adam bicycle bar()"
}