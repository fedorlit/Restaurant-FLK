package com.example.restaurantflk.core.data.remote

import com.example.restaurantflk.core.data.models.RestCountriesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RestCountriesApi {
    @GET("countries/v5")
    suspend fun getAll(
        @Header("Authorization") authorization: String,
        @Query("response_fields") responseFields: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): RestCountriesResponse
}
