package com.example.quickbite.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickbite.data.model.Restaurant
import com.example.quickbite.data.repository.FakeRestaurantRepository
import com.example.quickbite.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val repository: RestaurantRepository = FakeRestaurantRepository(),
) : ViewModel() {

    private val _allRestaurants = MutableStateFlow<List<Restaurant>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val restaurants: StateFlow<List<Restaurant>> = combine(
        _allRestaurants,
        _searchQuery,
        _selectedCategory,
    ) { all, query, category ->
        all.filter { restaurant ->
            val matchesQuery = query.isBlank() ||
                    restaurant.name.contains(query, ignoreCase = true) ||
                    restaurant.description.contains(query, ignoreCase = true) ||
                    restaurant.category.contains(query, ignoreCase = true)

            val matchesCategory = category.isBlank() ||
                    restaurant.category.equals(category, ignoreCase = true)

            matchesQuery && matchesCategory
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    init {
        loadRestaurants()
    }

    private fun loadRestaurants() {
        _allRestaurants.value = repository.getRestaurants()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }
}
