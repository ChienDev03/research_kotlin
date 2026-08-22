package com.example.hellokotlin

fun main() {
    val o1 = OperatorOverloading("ABC")
    val o2 = OperatorOverloading("XYZ")

    val result = o1 + o2
    println(result)
}

data class OperatorOverloading(val name: String) {

    operator fun plus(p: OperatorOverloading) = "${this.name} and ${p.name}"
}

