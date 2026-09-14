package com.example.restaurantflk.core.data.repoimpl

import com.example.restaurantflk.core.data.domain.CustomerRepository
import com.example.restaurantflk.core.data.models.Country
import com.example.restaurantflk.core.data.models.Customer
import com.example.restaurantflk.core.data.models.PhoneNumber
import com.example.restaurantflk.features.util.RequestState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.tasks.await

class CustomerRepoImpl: CustomerRepository {

    override fun getCurrentUserId(): String? =
        FirebaseAuth.getInstance().currentUser?.uid // obtiene el ID del usuario actual


    override suspend fun createCustomer(
        user: FirebaseUser,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val customerCollection = Firebase.firestore.collection("customer") // Acceder a la colección de customers
            val docRef = customerCollection.document(user.uid) //obtiene la referencia al documento del cliente
            val snapshot = docRef.get().await() //comprueba si el cliente ya existe

            if (!snapshot.exists()) { // si el cliente no existe, crea un nuevo documento
                val customerMap = mapOf(
                    "id" to user.uid,
                    "firstName" to (user.displayName?.split(" ")?.firstOrNull() ?: "Unknown"),
                    "lastName" to (user.displayName?.split(" ")?.lastOrNull() ?: "Unknown"),
                    "email" to (user.email ?: "Unknown"),
                    "photoUrl" to user.photoUrl.toString()
                )
                docRef.set(customerMap).await() //guarda los datos del cliente en la base de datos
            }
            onSuccess()
        }catch (e: Exception){
            RequestState.Error("Error creando cliente: ${e.message}")
        }
    }

    override suspend fun readCustomerFlow(): Flow<RequestState<Customer>> = channelFlow{
        try {
            val userId = getCurrentUserId()
            if(userId != null){
                val dataBase = Firebase.firestore
                dataBase.collection("customer")
                    .document(userId)
                    .snapshots()
                    .collectLatest { documentSnapshot ->
                        if (documentSnapshot.exists()){
                            val postalCode = (documentSnapshot.get("postalCode") as? Long)?.toInt()
                            val phoneNumberMap = documentSnapshot.get("phoneNumber") as? Map<*, *>
                            val phoneNumber = phoneNumberMap?.let {
                                val dialCode = (it["CountryCode"] as? Long)?.toInt()
                                val number = it["number"] as? String

                                if (dialCode!=null && number!=null){
                                    PhoneNumber(
                                        dialCode = dialCode,
                                        number = number
                                    )
                                }else{
                                    null
                                }
                            }

                            val countryMap = documentSnapshot.get("country") as? Map<*, *>
                            val country = countryMap?.let { map ->
                                val name = map["name"] as? String
                                val code = map["code"] as? String
                                val dialCode = (map["dialCode"] as? Long)?.toInt()
                                val flagUrl = map["flagUrl"] as? String
                                if(name !=null && code != null && dialCode != null && flagUrl != null){
                                    Country(
                                        name = name,
                                        code = code,
                                        dialCode = dialCode,
                                        flagUrl = flagUrl
                                    )
                                }else{
                                    null
                                }
                            }

                            val customer = Customer(
                                id = documentSnapshot.id,
                                firstName = documentSnapshot.get("firstName") as String,
                                lastName = documentSnapshot.get("lastName") as String,
                                email = documentSnapshot.get("email") as String,
                                city = documentSnapshot.get("city") as String?,
                                postalCode = postalCode,
                                phoneNumber = phoneNumber,
                                address = documentSnapshot.get("address") as String?,
                                country = country,
                                profilePictureUrl = documentSnapshot.get("photoUrl") as String?
                            )
                            send(RequestState.Success(data = customer))
                        }else{
                            send(RequestState.Error("Error obteniendo cliente"))
                        }
                    }
            }else{
                send(RequestState.Error("Usuario no disponible"))
            }
        }catch (e: Exception){
            send(RequestState.Error("Error obteniendo cliente: ${e.message}"))
        }
    }

    override suspend fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try{
            val userId = getCurrentUserId()
            if (userId != null){
                val firestore = Firebase.firestore
                val customerCollection = firestore.collection("customer")
                val existingCustomer = customerCollection
                    .document(customer.id)
                    .get().await()
                if (existingCustomer.exists()){
                    val phoneNumberMap = customer.phoneNumber?.let {
                        mapOf(
                            "CountryCode" to it.dialCode,
                            "number" to it.number
                        )
                    }

                    val countryMap = customer.country?.let {
                        mapOf(
                            "name" to it.name,
                            "code" to it.code,
                            "dialCode" to it.dialCode,
                            "flagUrl" to it.flagUrl
                        )
                    }

                    customerCollection
                        .document(customer.id)
                        .update(
                            mapOf(
                                "firstName" to customer.firstName,
                                "lastName" to customer.lastName,
                                "city" to customer.city,
                                "postalCode" to customer.postalCode,
                                "address" to customer.address,
                                "phoneNumber" to phoneNumberMap,
                                "country" to countryMap,
                                "photoUrl" to customer.profilePictureUrl
                            )
                        ).await()
                    onSuccess()
                } else{
                    RequestState.Error("Documento del cliente no encontrado")
                }
            }else{
                RequestState.Error("Usuario no disponible")
            }
        }catch (e: Exception){
            onError("Error actualizando cliente: ${e.message}")
        }
    }


    override suspend fun signOut(): RequestState<Unit> {
        return try {
            Firebase.auth.signOut()
            RequestState.Success(Unit)
        }catch (e: Exception){
            RequestState.Error("Error cerrando sesión: ${e.message}")
        }
    }
}