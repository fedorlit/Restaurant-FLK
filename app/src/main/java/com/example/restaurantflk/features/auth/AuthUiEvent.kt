package com.example.restaurantflk.features.auth

sealed class AuthUiEvent {
    data object Idle: AuthUiEvent()
    data object Loading: AuthUiEvent()
    data object Success: AuthUiEvent()
    data class Error(val message: String): AuthUiEvent()
}