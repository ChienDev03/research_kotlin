package com.example.quickbite.domain.payment


class EWalletPayment : PaymentMethod {

    override fun pay(
        amount: Double
    ): Boolean {

        println("Pay $amount by E-Wallet")

        return true
    }

    override fun displayName(): String {
        return "E-Wallet"
    }
}