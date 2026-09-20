package com.example.restaurantflk.core.data.domain

import com.example.restaurantflk.core.data.models.Product
import com.example.restaurantflk.features.util.RequestState
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun readNewProducts(): Flow<RequestState<List<Product>>>
    fun readPopularProducts(): Flow<RequestState<List<Product>>>
    fun readDiscountedProducts(): Flow<RequestState<List<Product>>>
    fun readProductsByCategory(category: String): Flow<RequestState<List<Product>>>

    fun readProductById(productId: String): Flow<RequestState<Product>>
}