package com.example.quickbite.data.model

data class CartItem(
    val food: Food,
    val quantity: Int,
    val size: String = "Regular",
    val addons: List<String> = emptyList()
) {
    fun calculatePrice(): Double {
        return food.price * quantity
    }
}
