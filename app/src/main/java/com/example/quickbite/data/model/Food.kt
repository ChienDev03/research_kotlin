package com.example.quickbite.data.model

data class Food(
    val id: Int,
    val restaurantId: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String = ""
)
