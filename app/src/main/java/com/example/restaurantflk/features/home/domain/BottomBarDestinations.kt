package com.example.restaurantflk.features.home.domain

import com.example.restaurantflk.features.nav.Screens
import com.example.restaurantflk.ui.theme.Resources

enum class BottomBarDestinations(
    val icon: Int,
    val title: String,
    val screen: Screens
) {
    ProductOverviewScreen(
        icon = Resources.Icon.Home,
        title = "Burgers",
        screen = Screens.ProductOverviewScreen
    ),
    CartScreen(
        icon = Resources.Icon.ShoppingCart,
        title = "Carrito",
        screen = Screens.Cart
    ),
    NotificationsScreen(
        icon = Resources.Icon.notifications,
        title = "Notificaciones",
        screen = Screens.Notifications
    ),
    CategoriesScreen(
        icon = Resources.Icon.Categories,
        title = "Categorías",
        screen = Screens.Categories
    )
}