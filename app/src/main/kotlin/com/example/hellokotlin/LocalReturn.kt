package com.example.hellokotlin

fun main() {
    printNumber()
}

fun <T> Iterable<T>.customForEach(action: (T) -> Unit): Unit {
    for (element in this) action(element)
}

fun printNumber() {
    val range = 1..10

    range.customForEach customForEach@{
        if (it == 5) {
            return@customForEach
        }

        println(it)
    }
    println("...")
}