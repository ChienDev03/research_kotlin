package com.example.quickbite.data.repository


import com.example.quickbite.data.model.Food
import com.example.quickbite.data.model.Restaurant

interface RestaurantRepository {

    fun getRestaurants(): List<Restaurant>

    fun getRestaurantById(id: Int): Restaurant?

    fun getFoodsByRestaurantId(
        restaurantId: Int
    ): List<Food>

    fun getFoodById(id: Int): Food?
}
