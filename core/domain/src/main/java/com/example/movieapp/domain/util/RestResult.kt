package com.example.movieapp.domain.util

sealed class RestResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : RestResult<T>(data)
    class Error<T>(message: String, data: T? = null) : RestResult<T>(data, message)
    class Loading<T>(data: T? = null) : RestResult<T>(data)
}