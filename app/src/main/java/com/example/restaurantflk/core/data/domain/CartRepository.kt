package com.example.restaurantflk.core.data.domain

import com.example.restaurantflk.core.data.models.CartItemUi
import com.example.restaurantflk.features.util.RequestState
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCartItems(): Flow<RequestState<List<CartItemUi>>>
    suspend fun increment(productId: String, productTitle: String? = null): RequestState<Unit>
    suspend fun decrement(productId: String): RequestState<Unit>
    suspend fun delete(productId: String): RequestState<Unit>
    suspend fun setQuantity(productId: String, quantity: Int): RequestState<Unit>
}