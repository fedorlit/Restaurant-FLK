package com.example.restaurantflk.features.auth

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.restaurantflk.R
import com.example.restaurantflk.core.data.auth.GoogleUIClient
import com.example.restaurantflk.features.components.GoogleButton
import com.example.restaurantflk.features.components.PrimaryButton
import com.example.restaurantflk.features.util.RequestState
import com.example.restaurantflk.ui.theme.FontSize
import com.example.restaurantflk.ui.theme.oswaldVariableFont
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun AuthScreen(
    navigateToHome: () -> Unit
){
    val context = LocalContext.current
    val activity = context as Activity
    val scope = rememberCoroutineScope()

    //Inyectar con Koin
    val authViewModel : AuthViewModel = koinViewModel()
    val googleAuthUIClient: GoogleUIClient = koinInject()

    var loadingState by remember { mutableStateOf(false) }

    Scaffold{ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.weight(0.8f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.burgers),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(
                            width = 420.dp,
                            height = 230.dp
                        )
                )
                Text(
                    text = stringResource(id = R.string.sign_in_text),
                    fontFamily = oswaldVariableFont(),
                    fontSize = FontSize.MEDIUM
                )
            }
            GoogleButton(
                loading = loadingState,
                onClick = {
                    scope.launch {
                        loadingState = true
                        try {
                            val authResult = googleAuthUIClient.signInWithGoogle(activity)
                            val user = authResult.user
                            if (user != null){
                                authViewModel.createCustomer(
                                    user = user,
                                    onSuccess = {
                                        // Éxito
                                    },
                                    onError = { error: String ->
                                        // Manejar error
                                    }
                                )
                                delay(2000)
                                navigateToHome()
                            }else{
                                RequestState.Error("No se pudo iniciar sesión con Google")
                            }
                        }catch (e: Exception){
                            RequestState.Error(e.message ?: "Error al iniciar sesión")
                        }
                    }
                },
                icon = painterResource(id = R.drawable.google_logo)
            )
            Spacer(modifier = Modifier.height(14.dp))
            PrimaryButton(
                text = stringResource(id = R.string.guest_text),
                icon = painterResource(id = R.drawable.log_in),
                onClick = {
                    scope.launch {
                        try {
                            val guestResult = googleAuthUIClient.guestSignIn()
                            val user = guestResult.user
                            if (user != null){
                                authViewModel.createCustomer(
                                    user = user,
                                    onSuccess = {
                                        // Éxito
                                    },
                                    onError = { error: String ->
                                        // Manejar error
                                    }
                                )
                                delay(2000)
                                navigateToHome()
                            }else{
                                RequestState.Error("No se pudo iniciar sesión como invitado")
                            }
                        }catch (e: Exception){
                            RequestState.Error(e.message ?: "Error al iniciar sesión como invitado")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}