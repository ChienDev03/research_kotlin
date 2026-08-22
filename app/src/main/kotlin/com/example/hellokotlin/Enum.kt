package com.example.hellokotlin

enum class Status(var status: String) {
    SUCCESS("thành công"),
    ERROR("thất bại"),
    PENDING("đang chờ")
}

fun main() {
    var status = Status.PENDING
    println(status.name)
    println(status.ordinal)

    for (s in Status.entries) {
        println("status: ${s.name} -> ${s.status}")
    }
}