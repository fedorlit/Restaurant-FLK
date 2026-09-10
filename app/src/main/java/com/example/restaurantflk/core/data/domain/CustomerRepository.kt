package com.example.restaurantflk.core.data.domain

import com.example.restaurantflk.features.util.RequestState
import com.google.firebase.auth.FirebaseUser

interface CustomerRepository {
    fun getCurrentUserId(): String?

    suspend fun createCustomer(
        user: FirebaseUser,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun signOut(): RequestState<Unit>
}