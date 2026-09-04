package com.example.quickbite.util

/**
 * A generic class that holds a value with its loading status.
 * @param <T> The type of data being handled.
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}