package com.example.quickbite.data.model

data class Order(
    val id: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val discount: Double,
    val total: Double,
    val paymentMethod: String,
    val status: OrderStatus
)
enum class OrderStatus {
    CONFIRMED,
    PREPARING,
    PICKING_UP,
    ON_THE_WAY,
    DELIVERED
}