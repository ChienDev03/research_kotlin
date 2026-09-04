package com.example.quickbite.domain.discount

open class DiscountCalculator {

    open fun calculateDiscount(
        subtotal: Double
    ): Double {
        return 0.0
    }

    open fun getDescription(): String {
        return "No discount"
    }
}