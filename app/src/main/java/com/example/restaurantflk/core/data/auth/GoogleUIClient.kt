package com.example.restaurantflk.core.data.auth

import android.app.Activity
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

// Clase encargada de gestionar la autenticación mediante Google, Firebase y usuarios anónimos.
class GoogleUIClient(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val serverClient: String
) {
    // Creamos CredentialManager solamente cuando se necesite.
    private val credManager by lazy{ CredentialManager.create(context)}

    // Inicia sesión utilizando una cuenta de Google.
    suspend fun signInWithGoogle(activity: Activity): AuthResult {

        // Configuración para obtener el ID Token de Google.
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(serverClient)

            // Permite mostrar cuentas aunque no estén autorizadas previamente.
            .setFilterByAuthorizedAccounts(false)
            .build()

        // Creamos la petición que se enviará a Credential Manager.
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        // Pedimos a Credential Manager que obtenga una credencial.
        val result = credManager.getCredential(activity,request)

        // Comprobamos qué tipo de credencial hemos recibido.
        val googleCred = when (val cred = result.credential) {

            // Si es una credencial personalizada...
            is CustomCredential ->{
                // Comprobamos que sea concretamente una credencial de Google ID Token.
                if (cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){

                    // Convertimos los datos de la credencial en un GoogleIdTokenCredential.
                    GoogleIdTokenCredential.createFrom(cred.data)
                }else error("Unsupported credential type: ${cred.type}")// No sabemos manejar este tipo de credencial.
            }
            else -> error("Unsupported credential class: ${result.credential::class.java.name}")// La credencial recibida no es compatible.
        }

        val idToken = googleCred.idToken // Obtenemos el ID Token que nos ha dado Google.
        val firebaseCred = GoogleAuthProvider.getCredential(idToken,null)// Convertimos el token de Google en una credencial que Firebase Authentication puede utilizar.
        return auth.signInWithCredential(firebaseCred).await() // Iniciamos sesión en Firebase utilizando la credencial obtenida de Google.
    }

    suspend fun guestSignIn(): AuthResult =
        auth.signInAnonymously().await()

    suspend fun signOut(){
        auth.signOut()

        // Intentamos limpiar también el estado de Credential Manager.
        runCatching {
            credManager.clearCredentialState(ClearCredentialStateRequest())
        }
    }

    val currentUser get() = auth.currentUser// Devuelve el usuario actualmente autenticado. Si no hay usuario, devuelve null.
}