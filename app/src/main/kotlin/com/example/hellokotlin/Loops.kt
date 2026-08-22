package com.example.hellokotlin

fun main() {
    loop@ for (i in 1..5) {
        for (j in 1..5) {
            print("i = $i, j = $j")
            if (i == j) {
                println("print i >j")
                break@loop
            }
            println("jjjjjjjjj $j")

        }
        println("iiiiiii $i")
    }

    // while
    println("=====while=====")
    var item = 10
    while (item < 15) {
        print("item $item ->")
        item++
    }
    println("")

    // do-while
    println("=====do while=====")
    var item2 = 0
    do {
        print(item2)
        item2++
    } while (item2 <= 10)
}
