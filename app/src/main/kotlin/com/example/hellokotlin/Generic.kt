package com.example.hellokotlin


fun main() {
    println("$-----")

    // 1. default
    println("default")
    val dogInvariance: Invariance<Dog> = Invariance(Dog("Dog"))
    println(dogInvariance.get().name)

    // 2. Variance
    println("Variance")
    val dogVariance: Variance<Dog> = Variance(Dog("Dog"))
    val catVariance: Variance<Animal1> = dogVariance
    println("${catVariance.get()}")

    // 3. Contravariant
    println("Contravariant")
    val animalIn: Convariance<Animal1> = Convariance()
    val catIn: Convariance<Cat> = animalIn
    catIn.consumerItem(Cat("Cat"))
}

open class Animal1(val name: String)
class Dog(name: String) : Animal1(name)
class Cat(name: String) : Animal1(name)

class Invariance<T>(val item: T) {
    fun get(): T {
        return item
    }
}

class Variance<out T>(val item: T) {
    fun get(): T {
        return item
    }
}

class Convariance<in T> {
    fun consumerItem(item: T) {
        if (item is Animal1) {
            println("print item $item ok")
        } else {
            println("this item is not Animal")
        }
    }
}

