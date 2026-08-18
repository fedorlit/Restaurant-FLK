package com.example.restaurantflk.core.data.repoimpl

import com.example.restaurantflk.core.data.domain.CustomerRepository
import com.example.restaurantflk.core.data.models.Customer
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class CustomerRepoImpl: CustomerRepository {

    override fun getCurrentUserId(): String? =
        FirebaseAuth.getInstance().currentUser?.uid // obtiene el ID del usuario actual


    override suspend fun createCustomer(user: FirebaseUser): Result<Unit> = runCatching { // runCatching para manejar excepciones
        val customerCollection = Firebase.firestore.collection("customer") // Acceder a la colección de customers
        val docRef = customerCollection.document(user.uid) //obtiene la referencia al documento del cliente
        val snapshot = docRef.get().await() //comprueba si el cliente ya existe

        if (!snapshot.exists()) { // si el cliente no existe, crea un nuevo documento
            val customer = Customer(
                id = user.uid,
                firstName = user.displayName?.split(" ")?.firstOrNull() ?: "Unknown",
                lastName = user.displayName?.split(" ")?.lastOrNull() ?: "Unknown",
                email = user.email ?: "Unknown"
            )
            docRef.set(customer).await() //guarda los datos del cliente en la base de datos
        }
        Unit // devuelve Unit si la operación es exitosa
    }
}