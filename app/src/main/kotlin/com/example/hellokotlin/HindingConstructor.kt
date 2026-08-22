package com.example.hellokotlin

fun main() {
    var sk = StudyKotlin()
    sk.run()
}

class StudyKotlin private constructor() {
    companion object {
        operator fun invoke() = StudyKotlin()
    }
    fun run(){}
}