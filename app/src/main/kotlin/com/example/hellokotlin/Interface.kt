package com.example.hellokotlin

interface PaymentService {
    fun pay(amount: Double) {
        println("So tien thanh toan la $amount")
    }

    fun refund(transactionId: String)
}

class MoMoPaymentService : PaymentService {

    override fun pay(amount: Double) {
        println("Pay $amount via Momo")
    }

    override fun refund(transactionId: String) {
        println("Refund $transactionId")
    }
}

fun main() {

    val payment = MoMoPaymentService()
    payment.pay(100000.0)
    payment.refund("transactionId")
}