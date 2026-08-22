package com.example.hellokotlin

fun main() {
    // if - else: có thể làm phương thức trả về giá trị
    var randomNumber = (1..100).random()
    println(randomNumber)

    if (randomNumber % 2 == 0) {
        println("number = $randomNumber => số này chẵn")
        
    } else {
        println("number = $randomNumber => số này lẻ")
    }


    // when <=> switch case
    when (randomNumber) {
        in 1..17 -> println("người chưa đủ tuổi")
        in 18..65 -> println("người trưởng thành")
        else -> println("người cao tuổi")
    }

}