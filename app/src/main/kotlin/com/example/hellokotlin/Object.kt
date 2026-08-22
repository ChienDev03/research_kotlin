package com.example.hellokotlin

// class AppConfig {
//   AppConfig._();

//   static final AppConfig instance = AppConfig._();

//   String appName = 'Plant Identifier';
// }

object AppConfig {
    var appName = "Kotlin"
}

class AppConfig2 {
    companion object {
        var appName = "Kotlin 2"
    }
}

fun main() {
    println(AppConfig.appName)
    println(AppConfig2.appName)


}