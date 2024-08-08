package com.betrybe.trybnb.data.repository

import com.betrybe.trybnb.data.models.AuthRequest
import com.betrybe.trybnb.data.models.BookingData
import com.betrybe.trybnb.data.models.BookingId
import com.betrybe.trybnb.data.models.TokenData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface OpenBookingServiceApi {
    @POST("auth")
    suspend fun getAuth(
        @Body body: AuthRequest
    ) : Response<TokenData>

    @GET("booking")
    suspend fun getBookingIds() : Response<List<BookingId>>

    @Headers ("Content-Type: application/json", "Accept: application/json")
    @GET("booking/{id}")
    suspend fun getBookingById(
        @Path("id") id: String
    ) : Response<BookingData>

    @Headers ("Content-Type: application/json", "Accept: application/json")
    @POST("booking")
    suspend fun createBooking(
        @Body body: BookingData
    ) : Response<BookingData>
}