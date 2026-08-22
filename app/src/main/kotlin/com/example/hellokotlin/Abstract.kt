package com.example.hellokotlin

abstract class Shape(val name: String) {
    abstract fun area(): Double
    fun describe() =
        println("$name co dien tich ${area()}")
}

class Circle(val radius: Double) : Shape("Circle") {
    override fun area() = Math.PI * radius * radius
}

class Square(val side: Double) : Shape("Square") {
    override fun area() = side * side
}

fun main() {
    val shapes = listOf(Circle(2.0), Square(3.0))
    shapes.forEach { it.describe() }
}
