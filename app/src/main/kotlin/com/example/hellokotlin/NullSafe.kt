package com.example.hellokotlin


fun main() {
    val name: String? = null

    // ?. safe call: trả về null thay vì crash nếu name null
    println(name?.length)

    // ?: elvis: cho giá trị mặc định khi vế trái null
    val length = name?.length ?: 0
    println("length = $length")

    // !! non-null assertion: ném NullPointerException nếu thực sự null
    val safeName: String = "Kotlin"
    println(safeName!!.uppercase())

    // let: chỉ chạy khối lệnh khi giá trị khác null
    name?.let {
        println("Tên là $it")
    } ?: println("Không có tên")
}
