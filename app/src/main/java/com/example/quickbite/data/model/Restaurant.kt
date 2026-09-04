package com.example.quickbite.data.model


data class Restaurant(
    val id: Int,
    val name: String,
    val description: String,
    val rating: Double,
    val deliveryTime: String,
    val category: String,
    val imageUrl: String = ""
)