package com.example.hellokotlin

fun main() {
    when (val result = callApi()) {
        is ApiResult.Success -> println(result.data)
        is ApiResult.Error -> println(result.message)
    }
}

sealed class ApiResult {
    data class Success(val data: String) : ApiResult()
    data class Error(val message: String) : ApiResult()
}

fun callApi(): ApiResult {
    val resp = (0..1).random()
    return if (resp == 0) {
        ApiResult.Error("404")
    } else {
        ApiResult.Success("200")
    }
}
