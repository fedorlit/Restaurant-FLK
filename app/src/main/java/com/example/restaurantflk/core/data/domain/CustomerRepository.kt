package com.example.restaurantflk.core.data.domain

import com.example.restaurantflk.core.data.models.Cart
import com.example.restaurantflk.core.data.models.Customer
import com.example.restaurantflk.features.util.RequestState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    fun getCurrentUserId(): String?

    suspend fun createCustomer(
        user: FirebaseUser,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    fun readCustomerFlow(): Flow<RequestState<Customer>>
    suspend fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
    suspend fun signOut(): RequestState<Unit>

    //funciones del carrito
    suspend fun addToCart(
        productId: String,
        productTitle: String,
        quantityToAdd: Int
    ): RequestState<Unit>

    suspend fun removeFromCart(
        productId: String,
        quantityToRemove: Int
    ): RequestState<Unit>

    suspend fun toggleFavourite(
        productId: String
    ): RequestState<Boolean>

    suspend fun isFavourite(
        productId: String
    ): RequestState<Boolean>

    fun readFavouriteIdFlow(): Flow<RequestState<Set<String>>>

    fun readBadgeCountFlow() : Flow<RequestState<Int>>

    fun readCartFlow(): Flow<RequestState<List<Cart>>>
    suspend fun deleteCartItem(productId: String): RequestState<Unit>
    suspend fun setCartQuantity(productId: String, newQuantity: Int): RequestState<Unit>
}