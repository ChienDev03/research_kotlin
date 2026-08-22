package com.example.hellokotlin

fun main() {
    var result = calculate(2.0, 3.0, ::number)
    println("result = $result")
    println("======")
    var result2 = calculate(3.0, 7.0, fun(x, y: Double): Double = x + y)
    println(result2)

    var result3 = calculate(4.0, 6.5) { x, y -> x * y }
    println(result3)

    // Lambda expression
    println("======")
    var acc1 = restApi { _, _ -> getUsername() }
    println(acc1)
    println("url = ${acc1.first}, method = ${acc1.second}, user = ${acc1.third}")
}

fun calculate(x: Double, y: Double, func: (Double, Double) -> Double): Double = func(x, y)

fun number(x: Double, y: Double): Double = (x + y)


data class Username(val firstName: String, val lastName: String)

fun getUsername(): Username = Username("John", "Doe")

fun restApi(
    url: String = "https://api.vnexpress.net",
    method: String = "GET",
    success: (String, Int) -> Username
): Triple<String, String, Username> {
    return Triple(url, method, Username("Bear", "Dev"))
}