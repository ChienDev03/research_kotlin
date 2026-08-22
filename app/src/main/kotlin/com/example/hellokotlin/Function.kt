package com.example.hellokotlin

fun main() {
    println("Function")
    hello()
    println("getAge() => ${getAge()}")
    println("sum(2, 3) => ${sum(2, 3)}")
    println("sumAll(1, 2, 3, 4, 5) => ${sumAll(1, 2, 3, 4, 5)}")
    printSum(1, 2, 3, 4, 5)
}

// void
fun hello(): Unit {
    println("Hello World, func Unit")
}

fun getAge() = 22

fun sum(a: Int, b: Int) = a + b

// unlimited params
fun sumAll(vararg numbers: Int): Int {
    var sum = 0
    for (i in numbers) sum += i
    return sum
}

fun printSum(vararg ints: Int) {
    val sum = sumAll(*ints)
    println("printSum => $sum")
}

