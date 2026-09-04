package com.example.quickbite.presentation.food

import androidx.lifecycle.ViewModel
import com.example.quickbite.data.model.CartItem
import com.example.quickbite.data.model.Food
import com.example.quickbite.data.repository.FakeRestaurantRepository
import com.example.quickbite.data.repository.RestaurantRepository
import com.example.quickbite.domain.food.Burger
import com.example.quickbite.domain.food.FoodProduct
import com.example.quickbite.domain.food.Pizza
import com.example.quickbite.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FoodDetailViewModel(
    private val repository: RestaurantRepository = FakeRestaurantRepository(),
) : ViewModel() {

    private val _foodState = MutableStateFlow<UiState<Food>>(UiState.Loading)
    val foodState: StateFlow<UiState<Food>> = _foodState.asStateFlow()

    private val currentFood: Food? get() = (_foodState.value as? UiState.Success)?.data

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    private val _extraCheese = MutableStateFlow(false)
    val extraCheese: StateFlow<Boolean> = _extraCheese.asStateFlow()

    private val _selectedSize = MutableStateFlow("Regular")
    val selectedSize: StateFlow<String> = _selectedSize.asStateFlow()

    fun loadFood(id: Int) {
        _foodState.value = UiState.Loading
        val result = repository.getFoodById(id)
        if (result != null) {
            _foodState.value = UiState.Success(result)
        } else {
            _foodState.value = UiState.Error("Food item not found")
        }
        _quantity.value = 1
        _extraCheese.value = false
        _selectedSize.value = "Regular"
    }

    fun setSize(size: String) {
        _selectedSize.value = size
    }

    fun toggleExtraCheese() {
        _extraCheese.value = !_extraCheese.value
    }

    fun increaseQuantity() {
        _quantity.value++
    }

    fun decreaseQuantity() {
        if (_quantity.value > 1) {
            _quantity.value--
        }
    }

    /**
     * Domain Polymorphism Factory:
     * Instantiates appropriate FoodProduct subclass based on category
     */
    fun getDomainFoodProduct(): FoodProduct? {
        val food = currentFood ?: return null
        return when (food.category) {
            "Burger" -> Burger(
                name = food.name,
                basePrice = food.price,
                extraCheese = _extraCheese.value,
            )
            "Pizza" -> Pizza(
                name = food.name,
                basePrice = food.price,
                extraCheese = _extraCheese.value,
            )
            else -> object : FoodProduct(food.name, food.price) {
                override fun calculatePrice(): Double = basePrice
            }
        }
    }

    /**
     * Calculates the unit price using domain polymorphism plus size adjustments
     */
    fun calculateUnitPrice(): Double {
        val product = getDomainFoodProduct() ?: return 0.0
        val baseCalculated = product.calculatePrice()
        val sizeAdjustment = when (_selectedSize.value) {
            "Large" -> 2.0
            "Small" -> -1.0
            else -> 0.0
        }
        return (baseCalculated + sizeAdjustment).coerceAtLeast(0.5)
    }

    fun calculateTotalPrice(): Double {
        return calculateUnitPrice() * _quantity.value
    }

    fun createCartItem(): CartItem? {
        val food = currentFood ?: return null
        val unitPrice = calculateUnitPrice()
        val addons = mutableListOf<String>()
        if (_extraCheese.value) {
            addons.add("Extra Cheese")
        }

        return CartItem(
            food = food.copy(price = unitPrice),
            quantity = _quantity.value,
            size = _selectedSize.value,
            addons = addons,
        )
    }
}
