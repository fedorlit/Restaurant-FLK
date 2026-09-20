package com.example.restaurantflk.core.data.repoimpl

import com.example.restaurantflk.core.data.domain.CustomerRepository
import com.example.restaurantflk.core.data.models.Cart
import com.example.restaurantflk.core.data.models.Country
import com.example.restaurantflk.core.data.models.Customer
import com.example.restaurantflk.core.data.models.PhoneNumber
import com.example.restaurantflk.features.util.RequestState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.tasks.await

private const val CUSTOMER_COLLECTION = "customer"
private const val CART_SUBCOLLECTION = "cart"
private const val FAVOURITE_SUBCOLLECTION = "favourite"

class CustomerRepoImpl : CustomerRepository {

    override fun getCurrentUserId(): String? =
        FirebaseAuth.getInstance().currentUser?.uid // obtiene el ID del usuario actual


    override suspend fun createCustomer(
        user: FirebaseUser,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val customerCollection =
                Firebase.firestore.collection(CUSTOMER_COLLECTION) // Acceder a la colección de customers
            val docRef =
                customerCollection.document(user.uid) //obtiene la referencia al documento del cliente
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
        } catch (e: Exception) {
            RequestState.Error("Error creando cliente: ${e.message}")
        }
    }

    override fun readCustomerFlow(): Flow<RequestState<Customer>> = channelFlow {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val dataBase = Firebase.firestore
                dataBase.collection(CUSTOMER_COLLECTION)
                    .document(userId)
                    .snapshots()
                    .collectLatest { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val postalCode = (documentSnapshot.get("postalCode") as? Long)?.toInt()
                            val phoneNumberMap = documentSnapshot.get("phoneNumber") as? Map<*, *>
                            val phoneNumber = phoneNumberMap?.let {
                                val dialCode = (it["CountryCode"] as? Long)?.toInt()
                                val number = it["number"] as? String

                                if (dialCode != null && number != null) {
                                    PhoneNumber(
                                        dialCode = dialCode,
                                        number = number
                                    )
                                } else {
                                    null
                                }
                            }

                            val countryMap = documentSnapshot.get("country") as? Map<*, *>
                            val country = countryMap?.let { map ->
                                val name = map["name"] as? String
                                val code = map["code"] as? String
                                val dialCode = (map["dialCode"] as? Long)?.toInt()
                                val flagUrl = map["flagUrl"] as? String
                                if (name != null && code != null && dialCode != null && flagUrl != null) {
                                    Country(
                                        name = name,
                                        code = code,
                                        dialCode = dialCode,
                                        flagUrl = flagUrl
                                    )
                                } else {
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
                                profilePictureUrl = documentSnapshot.get("photoUrl") as String?,
                                isAdmin = documentSnapshot.getBoolean("admin") ?: false
                            )
                            send(RequestState.Success(data = customer))
                        } else {
                            send(RequestState.Error("Error obteniendo cliente"))
                        }
                    }
            } else {
                send(RequestState.Error("Usuario no disponible"))
            }
        } catch (e: Exception) {
            send(RequestState.Error("Error obteniendo cliente: ${e.message}"))
        }
    }

    override suspend fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val firestore = Firebase.firestore
                val customerCollection = firestore.collection(CUSTOMER_COLLECTION)
                val existingCustomer = customerCollection
                    .document(customer.id)
                    .get().await()
                if (existingCustomer.exists()) {
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
                } else {
                    RequestState.Error("Documento del cliente no encontrado")
                }
            } else {
                RequestState.Error("Usuario no disponible")
            }
        } catch (e: Exception) {
            onError("Error actualizando cliente: ${e.message}")
        }
    }


    override suspend fun signOut(): RequestState<Unit> {
        return try {
            Firebase.auth.signOut()
            RequestState.Success(Unit)
        } catch (e: Exception) {
            RequestState.Error("Error cerrando sesión: ${e.message}")
        }
    }

    override suspend fun addToCart(
        productId: String,
        productTitle: String,
        quantityToAdd: Int
    ): RequestState<Unit> {
        return try {
            val uid = getCurrentUserId() ?: return RequestState.Error("Usuario no disponible")
            if (productId.isBlank()) return RequestState.Error("ID de producto no válido")
            if (quantityToAdd <= 0) return RequestState.Error("Cantidad no válida (min. 1)")

            val cartDoc = Firebase.firestore
                .collection(CUSTOMER_COLLECTION)
                .document(uid)
                .collection(CART_SUBCOLLECTION)
                .document(productId)

            Firebase.firestore.runTransaction { trx ->
                val snap = trx.get(cartDoc)
                if (snap.exists()) {
                    trx.update(
                        cartDoc, mapOf(
                            "quantity" to FieldValue.increment(quantityToAdd.toLong()),
                            "updatedAt" to FieldValue.serverTimestamp()
                        )
                    )
                } else {
                    trx.set(
                        cartDoc, mapOf(
                            "productId" to productId,
                            "quantity" to quantityToAdd,
                            "title" to productTitle,
                            "createdAt" to FieldValue.serverTimestamp(),
                            "updatedAt" to FieldValue.serverTimestamp()
                        )
                    )
                }
                Unit
            }.await()
            RequestState.Success(Unit)
        } catch (e: Exception) {
            RequestState.Error("Error añadiendo al carrito: ${e.message}")
        }
    }

    override suspend fun removeFromCart(
        productId: String,
        quantityToRemove: Int
    ): RequestState<Unit> {
        return try {
            val uid = getCurrentUserId() ?: return RequestState.Error("Usuario no disponible")
            if (productId.isBlank()) return RequestState.Error("ID de producto no válido")
            if (quantityToRemove <= 0) return RequestState.Error("Cantidad no válida (min. 1)")

            val cartDoc = Firebase.firestore
                .collection(CUSTOMER_COLLECTION)
                .document(uid)
                .collection(CART_SUBCOLLECTION)
                .document(productId)

            Firebase.firestore.runTransaction { trx ->
                val snap = trx.get(cartDoc)
                if (!snap.exists()) return@runTransaction
                val currentQty = (snap.getLong("quantity") ?: 0L).toInt()
                val newQty = currentQty - quantityToRemove
                if (newQty <= 0) {
                    trx.delete(cartDoc)
                } else {
                    trx.update(
                        cartDoc, mapOf(
                            "quantity" to quantityToRemove,
                            "createdAt" to FieldValue.serverTimestamp()
                        )
                    )
                }
                Unit
            }.await()
            RequestState.Success(Unit)
        } catch (e: Exception) {
            RequestState.Error("Error eliminando item del carrito: ${e.message}")
        }
    }

    override suspend fun toggleFavourite(productId: String): RequestState<Boolean> {
        return try {
            val uid = getCurrentUserId() ?: return RequestState.Error("Usuario no disponible")
            if (productId.isBlank()) return RequestState.Error("ID de producto no válido")

            val favDoc = Firebase.firestore
                .collection(CUSTOMER_COLLECTION)
                .document(uid)
                .collection(FAVOURITE_SUBCOLLECTION)
                .document(productId)

            val isFavouriteToggle = Firebase.firestore.runTransaction { trx ->
                val snap = trx.get(favDoc)
                if (snap.exists()) {
                    trx.delete(favDoc)
                    false
                } else {
                    trx.set(
                        favDoc, mapOf(
                            "productId" to productId,
                            "createdAt" to FieldValue.serverTimestamp()
                        )
                    )
                    true
                }
            }.await()
            RequestState.Success(isFavouriteToggle)
        } catch (e: Exception) {
            RequestState.Error("Error añadiendo a favoritos: ${e.message}")
        }
    }

    override suspend fun isFavourite(productId: String): RequestState<Boolean> {
        return try {
            val uid = getCurrentUserId() ?: return RequestState.Error("Usuario no disponible")
            if (productId.isBlank()) return RequestState.Error("ID de producto no válido")

            val isfavDoc = Firebase.firestore
                .collection(CUSTOMER_COLLECTION)
                .document(uid)
                .collection(FAVOURITE_SUBCOLLECTION)
                .document(productId)
                .get()
                .await()
            RequestState.Success(isfavDoc.exists())
        } catch (e: Exception) {
            RequestState.Error("Error comprobando favorito: ${e.message}")
        }
    }

    override fun readFavouriteIdFlow(): Flow<RequestState<Set<String>>> = channelFlow {
        try {
            val uid = getCurrentUserId()
            if (uid.isNullOrBlank()) {
                send(RequestState.Error("Usuario no disponible"))
                return@channelFlow
            }
            send(RequestState.Loading)

            Firebase.firestore
                .collection(CUSTOMER_COLLECTION)
                .document(uid)
                .collection(FAVOURITE_SUBCOLLECTION)
                .snapshots()
                .collectLatest { snapshot ->
                    val ids = snapshot.documents.map { it.id }.toSet()
                    send(RequestState.Success(ids))
                }
        } catch (e: Exception) {
            RequestState.Error("Error leyendo favoritos: ${e.message}")
        }
    }

    override fun readBadgeCountFlow(): Flow<RequestState<Int>> =channelFlow{
        try {
            val uid = getCurrentUserId()
            if (uid.isNullOrBlank()) {
                send(RequestState.Error("Usuario no disponible"))
                return@channelFlow
            }
            send(RequestState.Loading)

            Firebase.firestore
                .collection(CUSTOMER_COLLECTION)
                .document(uid)
                .collection(CART_SUBCOLLECTION)
                .snapshots()
                .collectLatest { snapshot ->
                    send(RequestState.Success(snapshot.size()))
                }
        } catch (e: Exception) {
            RequestState.Error("Error leyendo el tamaño de los items en el carrito: ${e.message}")
        }
    }

    override fun readCartFlow(): Flow<RequestState<List<Cart>>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCartItem(productId: String): RequestState<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun setCartQuantity(
        productId: String,
        newQuantity: Int
    ): RequestState<Unit> {
        TODO("Not yet implemented")
    }
}