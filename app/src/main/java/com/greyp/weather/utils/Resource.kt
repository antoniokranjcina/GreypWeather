package com.greyp.weather.utils

data class Resource<out T>(val status: Status, val data: T?, val error: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> = Resource(status = Status.SUCCESS, data = data, error = null)
        fun <T> error(errorText: String): Resource<T> = Resource(status = Status.ERROR, data = null, error = errorText)
        fun <T> loading(data: T?): Resource<T> = Resource(status = Status.LOADING, data = data, error = null)
        fun <T> empty(): Resource<T> = Resource(status = Status.EMPTY, data = null, error = null)
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    EMPTY
}