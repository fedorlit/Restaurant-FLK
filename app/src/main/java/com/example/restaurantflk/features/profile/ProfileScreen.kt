package com.example.restaurantflk.features.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.restaurantflk.features.components.InfoCard
import com.example.restaurantflk.features.components.LoadingCard
import com.example.restaurantflk.features.components.PrimaryButton
import com.example.restaurantflk.features.components.dialog.CountryPickerDialog
import com.example.restaurantflk.features.profile.components.ProfileForm
import com.example.restaurantflk.features.util.DisplayResult
import com.example.restaurantflk.features.util.RequestState
import com.example.restaurantflk.ui.theme.FontSize
import com.example.restaurantflk.ui.theme.IconPrimary
import com.example.restaurantflk.ui.theme.Resources
import com.example.restaurantflk.ui.theme.Surface
import com.example.restaurantflk.ui.theme.TextPrimary
import com.example.restaurantflk.ui.theme.oswaldVariableFont
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navigateBack: () -> Unit,
) {
    val profileViewModel = koinViewModel<ProfileViewModel>()
    val screenState = profileViewModel.screenState
    val screenReady = profileViewModel.screenReady
    val isFormValid = profileViewModel.isFormValid
    val countriesState = profileViewModel.countriesState

    var countryDialogOpen by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi perfil",
                        fontFamily = oswaldVariableFont(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back arrow icon",
                            tint = IconPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(paddingValues)
                .imePadding()
        ) {
            if (countryDialogOpen) {
                countriesState.DisplayResult(
                    onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
                    onSuccess = { countries ->
                        CountryPickerDialog(
                            countries = countries,
                            selectedCountry = screenState.country,
                            onDismiss = { countryDialogOpen = false },
                            onConfirmClick = { selectedCountry ->
                                profileViewModel.updateCountry(selectedCountry)
                                countryDialogOpen = false
                            }
                        )
                    },
                    onError = { message ->
                        InfoCard(
                            image = Resources.Icon.Error,
                            title = "Error",
                            subtitle = message
                        )
                    }
                )
            }
            screenReady.DisplayResult(
                onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
                onSuccess = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp, bottom = 24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfileForm(
                            modifier = Modifier.fillMaxWidth(),
                            firstname = screenState.firstName,
                            onFirstNameChange = profileViewModel::updateFirstName,
                            lastname = screenState.lastName,
                            onLastNameChange = profileViewModel::updateLastName,
                            email = screenState.email,
                            city = screenState.city,
                            onCityChange = profileViewModel::updateCity,
                            postalCode = screenState.postalCode,
                            onPostalCodeChange = profileViewModel::updatePostalCode,
                            address = screenState.address,
                            onAddressChange = profileViewModel::updateAddress,
                            phoneNumber = screenState.phoneNumber?.number,
                            onPhoneNumberChange = profileViewModel::updatePhoneNumber,
                            country = screenState.country,
                            onCountrySelect = {
                                countryDialogOpen = true
                            }
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        PrimaryButton(
                            text = "Actualizar",
                            icon = painterResource(Resources.Icon.Checkmark),
                            enabled = isFormValid,
                            onClick = {
                                profileViewModel.updateCustomer(
                                    onSuccess = {
                                        RequestState.Success("Datos actualizados")
                                        Toast.makeText(
                                            context,
                                            "Datos actualizados",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onError = { message ->
                                        RequestState.Error("Error actualizando datos: $message")
                                        Toast.makeText(
                                            context,
                                            "Error actualizando datos: $message",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        )
                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )
                    }
                },
                onError = { message ->
                    InfoCard(
                        image = Resources.Icon.Error,
                        title = "Error",
                        subtitle = message
                    )
                }
            )
        }
    }
}