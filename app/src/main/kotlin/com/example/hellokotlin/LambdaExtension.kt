package com.example.hellokotlin

fun main() {
    val stringBuilder = StringBuilder()
    stringBuilder.append("a")
    stringBuilder.append("b")
    stringBuilder.append("c")
    println(stringBuilder)

    val string = buildStr {
        append("1")
        append("2")
        append("3")
    }
    println(string)
    
    
}

inline fun buildStr(builder: StringBuilder.() -> Unit): String {
    val stringB = StringBuilder()
    stringB.builder()
    return stringB.toString()
}