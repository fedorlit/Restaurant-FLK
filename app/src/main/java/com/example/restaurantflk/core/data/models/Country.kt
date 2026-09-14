package com.example.restaurantflk.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Country (
    val code: String,
    val name: String,
    val dialCode: Int,
    val flagUrl: String?
)

@Serializable
data class RestCountriesResponse(
    @SerialName("data") val data: RestCountriesData? = null
)

@Serializable
data class RestCountriesData(
    @SerialName("objects") val objects: List<RestCountriesDto>? = null,
    @SerialName("meta") val meta: Meta? = null
)

@Serializable
data class Meta(
    @SerialName("more") val more: Boolean? = null
)

@Serializable
data class RestCountriesDto(
    @SerialName("names") val names: Names? = null,
    @SerialName("codes") val codes: Codes? = null,
    @SerialName("flag") val flag: Flag? = null,
    @SerialName("calling_codes") val calling_codes: List<String>? = null
)

@Serializable
data class Names(
    @SerialName("common") val common: String? = null
)

@Serializable
data class Codes(
    @SerialName("alpha_2") val alpha_2: String? = null
)

@Serializable
data class Flag(
    @SerialName("emoji") val emoji: String? = null
)

fun RestCountriesDto.toCountryOrNull(): Country? {
    val displayName = names?.common?.takeIf { it.isNotBlank() } ?: return null
    val code2 = codes?.alpha_2?.takeIf { it.isNotBlank() } ?: return null

    val dialText = calling_codes?.firstOrNull()?.takeIf { it.isNotBlank() } ?: "0"
    val dialInt = dialText.filter { it.isDigit() }.toIntOrNull() ?: return null

    // Se genera una URL de bandera utilizando el código alpha_2 con el servicio de FlagCDN
    val flagUrl = "https://flagcdn.com/w320/${code2.lowercase()}.png"

    return Country(
        name = displayName,
        dialCode = dialInt,
        code = code2,
        flagUrl = flagUrl
    )
}
