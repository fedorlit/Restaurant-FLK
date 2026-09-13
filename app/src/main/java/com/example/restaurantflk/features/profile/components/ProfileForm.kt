package com.example.restaurantflk.features.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.restaurantflk.features.components.BurgerTextField

@Composable
fun ProfileForm(
    modifier: Modifier = Modifier,
    firstname: String,
    onFirstNameChange: (String) -> Unit,
    lastname: String,
    onLastNameChange: (String) -> Unit,
    email: String,
    city: String?,
    onCityChange: (String) -> Unit,
    postalCode: Int?,
    onPostalCodeChange: (Int?) -> Unit,
    address: String?,
    onAddressChange: (String) -> Unit,
    phoneNumber: String?,
    onPhoneNumberChange: (String) -> Unit
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BurgerTextField(
            value = firstname,
            onValueChange = onFirstNameChange,
            placeholder = "Nombre",
            error = firstname.length !in 3..50
        )
        BurgerTextField(
            value = lastname,
            onValueChange = onLastNameChange,
            placeholder = "Apellido",
            error = lastname.length !in 3..50
        )
        BurgerTextField(
            value = email,
            onValueChange = {},
            placeholder = "Email",
            enabled = false
        )
        BurgerTextField(
            value = city ?: "",
            onValueChange = onCityChange,
            placeholder = "Ciudad",
            error = city?.length !in 3..50
        )
        BurgerTextField(
            value = "${postalCode ?: ""}",
            onValueChange = { onPostalCodeChange(it.toIntOrNull()) },
            placeholder = "Código Postal",
            error = postalCode == null || firstname.length !in 3..8,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )
        BurgerTextField(
            value = address?: "",
            onValueChange = onAddressChange,
            placeholder = "Dirección",
            error = address?.length !in 3..50,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )
        BurgerTextField(
            value = phoneNumber ?: "",
            onValueChange = onPhoneNumberChange,
            placeholder = "Teléfono",
            error = phoneNumber.toString().length !in 3..15,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }
}