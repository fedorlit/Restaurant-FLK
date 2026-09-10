package com.example.restaurantflk.features.home.domain

import com.example.restaurantflk.ui.theme.Resources

enum class DrawerItem(
    val title: String,
    val icon: Int
) {
    Profile(
        title = "Perfil",
        icon = Resources.Icon.Person
    ),
    Locations(
        title = "Localizaciones",
        icon = Resources.Icon.MapPin
    ),
    Rewards(
        title = "Recompensas",
        icon = Resources.Icon.Heart
    ),
    Offers(
        title = "Ofertas",
        icon = Resources.Icon.Gift
    ),
    ContactUs(
        title = "Contáctanos",
        icon = Resources.Icon.Edit
    ),
    SignOut(
        title = "Cerrar sesión",
        icon = Resources.Icon.SignOut
    ),
    AdminPanel(
        title = "Panel Admin",
        icon = Resources.Icon.Unlock
    )
}