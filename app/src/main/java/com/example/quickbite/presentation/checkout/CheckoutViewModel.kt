package com.example.quickbite.presentation.checkout

import androidx.lifecycle.ViewModel
import com.example.quickbite.data.model.CartItem
import com.example.quickbite.data.model.Order
import com.example.quickbite.data.model.OrderStatus
import com.example.quickbite.domain.discount.DiscountCalculator
import com.example.quickbite.domain.discount.FlashSaleDiscount
import com.example.quickbite.domain.discount.NormalDiscount
import com.example.quickbite.domain.discount.PremiumDiscount
import com.example.quickbite.domain.payment.CardPayment
import com.example.quickbite.domain.payment.CashPayment
import com.example.quickbite.domain.payment.EWalletPayment
import com.example.quickbite.domain.payment.PaymentMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CheckoutViewModel : ViewModel() {

    private val _paymentMethod = MutableStateFlow<PaymentMethod>(CashPayment())
    val paymentMethod: StateFlow<PaymentMethod> = _paymentMethod.asStateFlow()

    private val _discountCalculator = MutableStateFlow<DiscountCalculator>(NormalDiscount())
    val discountCalculator: StateFlow<DiscountCalculator> = _discountCalculator.asStateFlow()

    private val _lastOrder = MutableStateFlow<Order?>(null)
    val lastOrder: StateFlow<Order?> = _lastOrder.asStateFlow()

    fun selectCash() {
        _paymentMethod.value = CashPayment()
    }

    fun selectCard() {
        _paymentMethod.value = CardPayment()
    }

    fun selectWallet() {
        _paymentMethod.value = EWalletPayment()
    }

    fun selectNormalDiscount() {
        _discountCalculator.value = NormalDiscount()
    }

    fun selectPremiumDiscount() {
        _discountCalculator.value = PremiumDiscount()
    }

    fun selectFlashSale() {
        _discountCalculator.value = FlashSaleDiscount()
    }

    fun calculateTotal(items: List<CartItem>): Double {
        val subtotal = items.sumOf { it.calculatePrice() }
        val deliveryFee = if (items.isEmpty()) 0.0 else 2.0
        val discount = _discountCalculator.value.calculateDiscount(subtotal)
        return (subtotal + deliveryFee - discount).coerceAtLeast(0.0)
    }

    fun pay(amount: Double): Boolean {
        return _paymentMethod.value.pay(amount)
    }

    /**
     * Creates and packages the completed Order instance
     */
    fun placeOrder(items: List<CartItem>): Order? {
        val subtotal = items.sumOf { it.calculatePrice() }
        val deliveryFee = if (items.isEmpty()) 0.0 else 2.0
        val discount = _discountCalculator.value.calculateDiscount(subtotal)
        val total = (subtotal + deliveryFee - discount).coerceAtLeast(0.0)

        val isSuccess = pay(total)
        if (!isSuccess) return null

        val order = Order(
            id = "QB-${System.currentTimeMillis().toString().takeLast(4)}",
            items = items,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            discount = discount,
            total = total,
            paymentMethod = _paymentMethod.value.displayName(),
            status = OrderStatus.CONFIRMED,
        )

        _lastOrder.value = order
        return order
    }
}
