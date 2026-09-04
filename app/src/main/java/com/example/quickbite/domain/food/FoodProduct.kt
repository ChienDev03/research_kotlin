package com.example.quickbite.domain.food


abstract class FoodProduct(
    open val name: String,
    open val basePrice: Double
) {

    abstract fun calculatePrice(): Double

    fun displayName(): String {
        return name
    }
}