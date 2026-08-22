package com.example.hellokotlin

fun main() {
    val studentIt1 = StudentIt(Kotlin(), Dart())
    studentIt1.myLanguage()
}

interface LearKotlin {
    fun learKotlin()
}

interface LearDart {
    fun learDart()
}

class Kotlin : LearKotlin {
    override fun learKotlin() {
        val name = "Kotlin"
        println("Learning $name")
    }
}

class Dart : LearDart {
    override fun learDart() {
        val name = "Dart"
        println("Learning $name")
    }
}

class StudentIt(kotlin: Kotlin, dart: Dart) :
    LearKotlin by kotlin, LearDart by dart {
    fun myLanguage() {
        learDart()
        learKotlin()
    }
}

