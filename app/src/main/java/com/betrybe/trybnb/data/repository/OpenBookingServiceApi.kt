package com.betrybe.trybnb.data.repository

import com.betrybe.trybnb.data.models.AuthRequest
import com.betrybe.trybnb.data.models.BookingData
import com.betrybe.trybnb.data.models.BookingIdsData
import com.betrybe.trybnb.data.models.TokenData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OpenBookingServiceApi {
    @POST("auth")
    suspend fun getAuth(
        @Body body: AuthRequest
    ) : Response<TokenData>

    @GET("booking")
    suspend fun getBookingIds() : Response<BookingIdsData>

    @GET("booking/{id}")
    suspend fun getBookingById(
        @Query("id") id: Int
    ) : Response<BookingData>
}