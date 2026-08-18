package com.example.restaurantflk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.restaurantflk.features.nav.NavGraph
import com.example.restaurantflk.ui.theme.RestaurantFLKTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RestaurantFLKTheme {
                NavGraph()
            }
        }
    }
}