package com.example.hellokotlin

fun main() {
    println("==v1==")
    println(fibonacciV1(10))
    println("==v1==")
    println(fibonacciV2(10))

}

// Fun bình thường
fun fibonacciV1(n: Int): Int {
    if (n == 0 || n == 1) return n
    return fibonacciV1(n - 1) + fibonacciV1(n - 2)
}

// Fun có dùng tail recursion
tailrec fun fibonacciV2(n: Int, a: Int = 0, b: Int = 1): Int =
    when (n) {
        0 -> a
        1 -> b
        else -> fibonacciV2(n - 1, b, a + b)
    }