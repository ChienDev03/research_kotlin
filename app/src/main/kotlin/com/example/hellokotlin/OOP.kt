package com.example.hellokotlin

fun main() {
    val user = UserImpl()
    user.login()
    println(user.name)

    println(user.actionHelp())
}

abstract class User {
    val name: String = "John"
    val age: Int = 20
    val email: String = "[EMAIL_ADDRESS]"
    abstract fun login()
}

class UserImpl : User(), Help {
    override fun login() {
        println("User is logging in")
    }
}


interface Help {
    fun actionHelp() : Any {return println("How can i help you?")}
}
