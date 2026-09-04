package com.example.quickbite.domain.discount

class FlashSaleDiscount : DiscountCalculator() {

    override fun calculateDiscount(
        subtotal: Double
    ): Double {

        return minOf(5.0, subtotal * 0.2)
    }

    override fun getDescription(): String {
        return "Flash Sale discount"
    }
}