package com.example.hellokotlin

fun main() {
    val password = "13@pass"

    val result = password.validatePassword()
    println(password)
    println("password is valid = $result")

    val lib = Library()
    lib.exFunc()

    val number = 100
    val result1 = number cong 200
    println(result1)
    println(number.cong(200))
}

// Extension function
fun String.validatePassword(): Boolean = length >= 8

class Library(val name: String = "Kotlin Extension Demo")

fun Library.exFunc() = println("exFunc called on $name")

// Infix function
infix fun Int.cong(number: Int) = this.plus(number)