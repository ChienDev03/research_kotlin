package com.example.hellokotlin

fun main() {
    var list = listOf("a", "b", "c")
    println(list)

    val list1 = mutableListOf(1, 2, 3, 4)
    list1.addAll(listOf(6, 7))
    println(list1)

    var map = mapOf("id1" to 1, "id2" to 2, "id3" to 3, "id4" to 4)
    var customMap = map.entries.map { it.key + ":" + it.value }
    println(customMap)

    map = map + mapOf("id5" to 5, "id6" to 6)
    println("map $map")

}

