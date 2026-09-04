package com.example.quickbite.domain.payment


class CashPayment : PaymentMethod {

    override fun pay(
        amount: Double
    ): Boolean {

        println("Pay $amount by Cash")

        return true
    }

    override fun displayName(): String {
        return "Cash"
    }
}