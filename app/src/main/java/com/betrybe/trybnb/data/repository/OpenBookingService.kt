package com.betrybe.trybnb.data.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenBookingService {
    private const val BASE_URL = "https://restful-booker.herokuapp.com/"

    val instance: OpenBookingServiceApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(OpenBookingServiceApi::class.java)
    }
}