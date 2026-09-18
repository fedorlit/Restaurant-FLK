package com.example.restaurantflk.core.data.models

import com.example.restaurantflk.ui.theme.Resources
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class Product(
    val id: String,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val title: String,
    val description: String,
    val category: String,
    val allergyAdvice: String,
    val energyValue: Int?,
    val ingredients: String,
    val price: Double,
    val productImage: String,
    val isPopular: Boolean = false,
    val isNew: Boolean = false,
    val isDiscounted: Boolean = false
)

enum class ProductCategory(
    val title: String,
    val icon: Int
) {
    Burgers(
        title = "Hamburguesas",
        icon = Resources.Icon.Burger
    ),
    Nuggets(
        title = "Nuggets",
        icon = Resources.Icon.Nuggets
    ),
    Wraps(
        title = "Burritos",
        icon = Resources.Icon.Wraps
    ),
    Desserts(
        title = "Postres",
        icon = Resources.Icon.Desserts
    ),
    Sauces(
        title = "Salsas",
        icon = Resources.Icon.Sauces
    ),
    Fries(
        title = "Patatas fritas",
        icon = Resources.Icon.Fries
    ),
    Drinks(
        title = "Bebidas",
        icon = Resources.Icon.Drinks
    )
}
