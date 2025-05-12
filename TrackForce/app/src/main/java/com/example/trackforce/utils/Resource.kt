package com.example.trackforce.utils

sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(
        throwable: Throwable? = null,
        message: String? = null,
        data: T? = null
    ) : Resource<T>(
        error = throwable,
        message = message ?: throwable?.message,
        data = data
    )
    class Loading<T>(data: T? = null) : Resource<T>(data = data)

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading

    companion object {
        fun <T> success(data: T) = Success(data)
        fun <T> error(throwable: Throwable? = null, message: String? = null, data: T? = null) = 
            Error<T>(throwable, message, data)
        fun <T> loading(data: T? = null) = Loading(data)
    }
} 