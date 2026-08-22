package com.example.hellokotlin

// Deconstructing value là một khái niệm và là tính năng hữu ích của Kotlin
// Nó cho phép bạn "mổ xẻ", "tách" hoặc "trích xuất" các thành phần từ
// một đối tượng (class) hoặc một cấu trúc dữ liệu phức tạp (tuple, map, object, data class)
// thành các biến riêng lẻ một cách ngắn gọn và dễ đọc.

// ứng dụng vào những case: 
// 1. Khai báo cùng lúc nhiều biến từ class
// 2. Truyền đối tượng vào function và nhận nhiều giá trị trả về
// 3. Làm việc với data class
// 4. Truy cập value từ map
// vd:

fun getUser(name: String = "John", age: Int = 25) = Pair(name, age)
fun getUser1() = Triple("Bears", 25, "Vietnam")


fun main() {

    try {

    } catch (e: Exception) {
        println("Error: $e")
    } finally {
        println("Finally")
    }

    // 1. Khai báo cùng lúc nhiều biến từ class
    val dog = Animal(
        "Dog",
        "Mammal",
        3
    )
    val getUser = getUser()
    val getUser1 = getUser1()
    println("abc ${getUser.first}")
    println(getUser.second)
    println("abc ${getUser1.first}")
    print(getUser1.second)
    println(getUser1.third)

    // Deconstructing
    val (name, age) = dog
    println("name = $name, age = $age")

    // 2. Truyền đối tượng vào function và nhận nhiều giá trị trả về
    val (name2, age2) = getUser("Chien", 23)
    println("name2 = $name2, age2 = $age2")

    // 3. Làm việc với data class
    val person = Developer("Gauuuu", 23, "Kotlin")
    val (namePerson, agePerson, langPerson) = person
    println("namePerson = $namePerson, agePerson = $agePerson, langPerson = $langPerson")

    // 4. Duyệt qua Map sử dụng Destructuring (Map.Entry)
    val map = mapOf("name" to "John", "age" to 25)
    for ((key, value) in map) {
        println("key = $key, value = $value")
    }
}


class Animal(val name: String, val type: String, val age: Int) {
    operator fun component1() = name
    operator fun component2() = type
}

data class Developer(val nameDev: String, val age: Int, val lang: String)
