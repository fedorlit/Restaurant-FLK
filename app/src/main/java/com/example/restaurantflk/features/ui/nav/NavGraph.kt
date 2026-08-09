package com.example.restaurantflk.features.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurantflk.features.splash.SplashScreen

@Composable
fun NavGraph(startDestination: Screens = Screens.SplashScreen) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable<Screens.SplashScreen>{
            SplashScreen()
        }
    }
}