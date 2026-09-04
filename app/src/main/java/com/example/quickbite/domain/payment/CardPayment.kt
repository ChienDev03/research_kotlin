package com.example.quickbite.domain.payment


class CardPayment : PaymentMethod {

    override fun pay(
        amount: Double
    ): Boolean {

        println("Pay $amount by Credit Card")

        return true
    }

    override fun displayName(): String {
        return "Credit Card"
    }
}