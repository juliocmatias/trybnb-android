package com.betrybe.trybnb.data.models

sealed class ServiceResponse<out T> {

    data class SuccessResponse<out T>(
        val data: T,
        val message: String ) : ServiceResponse<T>()

    data class ErrorResponse(val error: String) : ServiceResponse<Nothing>()
}
