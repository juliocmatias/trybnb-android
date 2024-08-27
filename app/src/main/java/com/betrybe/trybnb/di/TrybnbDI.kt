package com.betrybe.trybnb.di

import com.betrybe.trybnb.data.api.OpenBookingServiceApi
import com.betrybe.trybnb.data.datasource.network.LoginDataSource
import com.betrybe.trybnb.data.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object TrybnbDI {

    private const val BASE_URL = "https://restful-booker.herokuapp.com/"

    @Provides
    fun getLoginRepository(loginDataSource: LoginDataSource): LoginRepository {
        return LoginRepository(loginDataSource)
    }

    @Provides
    fun getLoginDataSource(openBookingServiceApi: OpenBookingServiceApi): LoginDataSource {
        return LoginDataSource(openBookingServiceApi)
    }

    @Provides
    fun getOpenBookingServiceApi(): OpenBookingServiceApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(OpenBookingServiceApi::class.java)
    }

}