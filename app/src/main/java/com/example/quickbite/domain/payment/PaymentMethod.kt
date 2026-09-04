package com.example.quickbite.domain.payment


interface PaymentMethod {

    fun pay(amount: Double): Boolean

    fun displayName(): String
}