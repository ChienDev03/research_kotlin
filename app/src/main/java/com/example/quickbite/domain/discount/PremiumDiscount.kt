package com.example.quickbite.domain.discount

class PremiumDiscount : DiscountCalculator() {

    override fun calculateDiscount(
        subtotal: Double
    ): Double {

        return subtotal * 0.1
    }

    override fun getDescription(): String {
        return "10% Premium discount"
    }
}
