package com.example.quickbite.domain.discount

class NormalDiscount : DiscountCalculator() {

    override fun calculateDiscount(
        subtotal: Double
    ): Double {
        return 0.0
    }

    override fun getDescription(): String {
        return "No discount"
    }
}