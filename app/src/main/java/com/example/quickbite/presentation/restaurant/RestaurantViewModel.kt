package com.example.quickbite.presentation.restaurant

import androidx.lifecycle.ViewModel
import com.example.quickbite.data.model.Food
import com.example.quickbite.data.model.Restaurant
import com.example.quickbite.data.repository.FakeRestaurantRepository
import com.example.quickbite.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RestaurantViewModel(
    private val repository: RestaurantRepository =
        FakeRestaurantRepository()
) : ViewModel() {

    private val _restaurant =
        MutableStateFlow<Restaurant?>(null)

    val restaurant: StateFlow<Restaurant?> =
        _restaurant.asStateFlow()

    private val _foods =
        MutableStateFlow<List<Food>>(emptyList())

    val foods: StateFlow<List<Food>> =
        _foods.asStateFlow()

    fun loadRestaurant(id: Int) {

        _restaurant.value =
            repository.getRestaurantById(id)

        _foods.value =
            repository.getFoodsByRestaurantId(id)
    }
}