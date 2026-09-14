package com.example.restaurantflk.features.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.restaurantflk.core.data.models.Country
import com.example.restaurantflk.features.components.BurgerSelectTextField
import com.example.restaurantflk.features.components.BurgerTextField

@Composable
fun ProfileForm(
    modifier: Modifier = Modifier,
    firstname: String,
    onFirstNameChange: (String) -> Unit,
    lastname: String,
    onLastNameChange: (String) -> Unit,
    email: String,
    country: Country?,
    onCountrySelect: () -> Unit,
    city: String?,
    onCityChange: (String) -> Unit,
    postalCode: Int?,
    onPostalCodeChange: (Int?) -> Unit,
    address: String?,
    onAddressChange: (String) -> Unit,
    phoneNumber: String?,
    onPhoneNumberChange: (String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
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

        BurgerSelectTextField(
            modifier = Modifier.fillMaxWidth(),
            text = country?.name ?: "",
            iconUrl = country?.flagUrl,
            onClick = onCountrySelect,
            placeholder = "País"
        )

        BurgerTextField(
            value = city ?: "",
            onValueChange = onCityChange,
            placeholder = "Ciudad",
            error = city?.length !in 3..50
        )

        BurgerTextField(
            value = postalCode?.toString() ?: "",
            onValueChange = { value ->
                onPostalCodeChange(value.toIntOrNull())
            },
            placeholder = "Código Postal",
            error = postalCode == null ||
                    postalCode.toString().length !in 3..8,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        BurgerTextField(
            value = address ?: "",
            onValueChange = onAddressChange,
            placeholder = "Dirección",
            error = address?.length !in 3..50,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            BurgerSelectTextField(
                modifier = Modifier.width(120.dp),
                text = country?.let { "+${it.dialCode}" } ?: "+-",
                iconUrl = country?.flagUrl,
                onClick = onCountrySelect,
                placeholder = "+-"
            )

            Spacer(modifier = Modifier.width(12.dp))

            BurgerTextField(
                modifier = Modifier.weight(1f),
                value = phoneNumber ?: "",
                onValueChange = onPhoneNumberChange,
                placeholder = "Teléfono",
                error = phoneNumber?.length !in 3..15,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }
}