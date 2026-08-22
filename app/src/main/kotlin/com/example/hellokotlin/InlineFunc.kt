package com.example.hellokotlin

import kotlin.math.sqrt

fun main() {
    val total = sum(1, 2) { a, b -> a + b }
    println(total)

    val sqrt = sqrt(4.0) {
        sqrt(it)
    }
    println(sqrt)

}

inline fun sum(a: Int, b: Int, action: (Int, Int) -> Int) = action(a, b)
fun sqrt(a: Double, action: (Double) -> Double) = action(a)