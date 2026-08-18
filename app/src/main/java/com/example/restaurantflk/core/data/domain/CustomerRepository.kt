package com.example.restaurantflk.core.data.domain

import com.google.firebase.auth.FirebaseUser

interface CustomerRepository {
    fun getCurrentUserId(): String?

    suspend fun createCustomer(user: FirebaseUser):Result<Unit>
}