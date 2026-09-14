package com.example.restaurantflk.core.data.domain

import com.example.restaurantflk.core.data.models.Country
import com.example.restaurantflk.core.data.models.toCountryOrNull
import com.example.restaurantflk.core.data.remote.RestCountriesApi
import com.example.restaurantflk.features.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

interface CountryRepository {
    suspend fun fetchCountries(): Flow<RequestState<List<Country>>>
}

class CountryRepositoryImpl(
    private val api: RestCountriesApi
): CountryRepository {
    override suspend fun fetchCountries(): Flow<RequestState<List<Country>>> = flow {
        try {
            emit(RequestState.Loading)
            val allCountries = mutableListOf<Country>()
            var currentOffset = 0
            var hasMore = true

            while (hasMore) {
                val response = withContext(Dispatchers.IO) {
                    api.getAll(
                        authorization = "Bearer rc_live_a15c22c83d4f4cc6ac939d154d65f8ae",
                        responseFields = "names.common,codes.alpha_2,calling_codes,flag.emoji",
                        limit = 100,
                        offset = currentOffset
                    )
                }

                val batch = response.data?.objects
                    ?.mapNotNull { it.toCountryOrNull() }
                    ?: emptyList()
                
                allCountries.addAll(batch)

                hasMore = response.data?.meta?.more ?: false
                if (hasMore) {
                    currentOffset += 100
                }
            }

            val countries = allCountries
                .distinctBy { it.code }
                .sortedBy { it.name }

            if (countries.isEmpty()) {
                emit(RequestState.Error("No se encontraron países en la respuesta de la API."))
            } else {
                emit(RequestState.Success(countries))
            }
        } catch (e: Exception) {
            emit(RequestState.Error("Error al conectar con la API: ${e.message ?: "Error desconocido"}"))
        }
    }
}
