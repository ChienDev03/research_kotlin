package com.example.hellokotlin

fun main() {
    // list: mutable và immutable
    val list = listOf("a", "b", "c")
    // list.add(0, 6)
    println(list)

    var list2 = mutableListOf(1, 2, 3, 4)
    list2.add(0, 6)
    list2[3] = 10
    println(list2)
    println(list2.sorted())

    // set
    val set = setOf(1, 9, 7, 8, 8, 2, 2, 3, 4, 4, 5, 1, 6)
    println(set.sorted())

    // map
    var map = mutableMapOf<String, Int>(
        "name" to 1,
        "age" to 2,
        "address" to 3,
        "city" to 4,
        "country" to 5,
    )
    map["age"] = 20
    println(map["age"])
    println(map["address"])

    // iterate
    for (key in map.keys) {
        println("key = $key, value = ${map[key]}")
    }
}