package com.betrybe.trybnb.data.repository

import com.betrybe.trybnb.data.models.AuthRequest
import com.betrybe.trybnb.data.models.TokenData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenBookingServiceApi {
    @POST("auth")
    suspend fun getAuth(
        @Body body: AuthRequest
    ) : Response<TokenData>
}