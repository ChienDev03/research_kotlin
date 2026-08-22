package com.example.hellokotlin

fun main() {
    val student1 = Student("John", 20)
    val student2 = Student("John", 20)
    val student3 = Student("Jane", 21)
    println(student1)
    println(student1 == student2)
    println(student1 == student3)
}

data class Student(val name: String, val age: Int)
