package com.example.hellokotlin

open class Film(var name: String = "") {
    init {
        println("Film init")
        name = name.trim()
    }

    open fun showNameFilm() {
        println("Name: $name")
    }
}


class Marvel(name: String = "") : Film(name) {
    init {
        println("Marvel init")
    }

    override fun showNameFilm() {
        super.showNameFilm()
        println("This film is from Marvel")
    }
}


fun main() {
    val marvel = Marvel(" Avengers ")
    marvel.showNameFilm()
}