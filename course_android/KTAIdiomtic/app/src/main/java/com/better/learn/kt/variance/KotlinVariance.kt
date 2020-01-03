package com.better.learn.kt.variance

/**
 * Created by better on 2020-01-02 21:25.
 */

// array invariance
val b: Array<B> = arrayOf(B())
//val a: Array<A> = b


//generic default invariance
class Box<T>

val boxb: Box<B> = Box()
//val boxa: Box<A> = boxb


//generic out

class BoxOut<out T>

val boxOutB: BoxOut<B> = BoxOut()

val boxOutA: BoxOut<A> = boxOutB

//generic in

class BoxIn<out T>

val boxInB: BoxIn<B> = BoxIn()
//这里应该报错才是
val boxInA: BoxIn<A> = boxInB


// demo on idiomatic  covariance
interface Producer<out T> {
    fun produce(): T
//    fun input(t:T):Boolean
}

class NumberProducer : Producer<Number> {
    override fun produce(): Number = Math.random()
}

val producer: Producer<Any> = NumberProducer()

// demo on idiomatic contravariance
interface Consumer<in T> {
    fun consume(input: T): Boolean
//    fun  consume():T
}

class NumberConsumer : Consumer<Number> {
    override fun consume(input: Number): Boolean = true
}

fun checkConsumer(consumer: NumberConsumer) {
    val intConsumer: Consumer<Int> = consumer
}
