package com.betrybe.trybnb.data.datasource.network

import com.betrybe.trybnb.data.models.AuthRequest
import com.betrybe.trybnb.data.api.OpenBookingServiceApi
import retrofit2.HttpException
import javax.inject.Inject

class LoginDataSource @Inject constructor(private val openBookingServiceApi: OpenBookingServiceApi) {

    suspend fun getCurrencyAuth(login: String, password: String) : String? {
        val response = openBookingServiceApi.getAuth(AuthRequest(login, password))

        return if (response.isSuccessful) {
            response.body()?.token
        } else {
            throw HttpException(response)
        }
    }
}