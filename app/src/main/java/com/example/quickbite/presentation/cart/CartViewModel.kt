package com.example.quickbite.presentation.cart

import androidx.lifecycle.ViewModel
import com.example.quickbite.data.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {

    private val _items =
        MutableStateFlow<List<CartItem>>(emptyList())

    val items: StateFlow<List<CartItem>> =
        _items.asStateFlow()

    fun addItem(item: CartItem) {

        _items.update { currentItems ->

            val existing =
                currentItems.find {
                    it.food.id == item.food.id
                }

            if (existing == null) {

                currentItems + item

            } else {

                currentItems.map {

                    if (it.food.id == item.food.id) {

                        it.copy(
                            quantity =
                                it.quantity + item.quantity
                        )

                    } else {
                        it
                    }
                }
            }
        }
    }

    fun increase(item: CartItem) {

        _items.update { items ->

            items.map {

                if (it.food.id == item.food.id) {

                    it.copy(
                        quantity = it.quantity + 1
                    )

                } else {
                    it
                }
            }
        }
    }

    fun decrease(item: CartItem) {

        _items.update { items ->

            items.mapNotNull {

                if (it.food.id == item.food.id) {

                    if (it.quantity <= 1) {
                        null
                    } else {
                        it.copy(
                            quantity = it.quantity - 1
                        )
                    }

                } else {
                    it
                }
            }
        }
    }

    fun subtotal(): Double {

        return _items.value.sumOf {
            it.calculatePrice()
        }
    }

    fun clear() {
        _items.value = emptyList()
    }
}