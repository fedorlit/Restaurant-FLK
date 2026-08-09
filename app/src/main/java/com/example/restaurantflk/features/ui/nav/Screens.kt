package com.example.restaurantflk.features.ui.nav

import kotlinx.serialization.Serializable


@Serializable
sealed class Screens {
    @Serializable
    data object SplashScreen : Screens()

    @Serializable
    data object AuthScreen : Screens()

    @Serializable
    data object HomeGraph : Screens()
}