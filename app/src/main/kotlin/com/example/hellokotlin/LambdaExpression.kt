package com.example.hellokotlin

fun main() {
    val square = { x: Int -> x * x }
    println("print square: ${square(5)}")

    val numbers = listOf(1, 2, 3, 4)
    val x2Number = numbers.map { it * 2 }
    println("x2 number $x2Number")
    
    // không dùng lambda
    for (m in numbers) {
        val sq = m * 2
        println("print no lambda $sq")
    }

    val sumSquare = numbers.map { n ->
        val sq = n * n
        sq
    }

    println("print have lambda $sumSquare")
}