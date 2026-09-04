package com.example.quickbite.domain.food

class Pizza(
    override val name: String,
    override val basePrice: Double,
    private val extraCheese: Boolean = false
) : FoodProduct(name, basePrice) {

    override fun calculatePrice(): Double {

        var price = basePrice

        if (extraCheese) {
            price += 1.5
        }

        return price
    }
}