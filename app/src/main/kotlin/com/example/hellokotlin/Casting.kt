package com.example.hellokotlin

// Casting type <=> ép kiểu
fun main() {
    var float = 3.141545f
    var int = float.toInt()
    println("int => $int")

    val pc = showOsPc(Mac())

    if (pc is Window) {
        pc.showWindow()
    } else if (pc is Mac) {
        pc.showMac()
    }

    val pc2 = pc as Mac
    pc2.showMac()

}

fun showOsPc(pc: PC) = pc


open class PC

class Window : PC() {
    fun showWindow() = println("show Window")

    fun sayHello() = println("Hello Window")
}

class Mac : PC() {
    fun showMac() = println("show Mac")
}