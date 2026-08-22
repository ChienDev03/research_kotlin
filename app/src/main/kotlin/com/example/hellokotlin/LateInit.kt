package com.example.hellokotlin

fun main() {
    // late init
    val people = People()
    people.name = "Bears"
    people.showName()
}
class People {
    lateinit var name: String
    
    fun showName() = println("tôi tên là $name")
}