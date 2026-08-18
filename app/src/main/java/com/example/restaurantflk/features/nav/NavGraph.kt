package com.example.restaurantflk.features.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurantflk.features.splash.SplashScreen
import com.example.restaurantflk.features.auth.AuthScreen
import com.example.restaurantflk.features.home.HomeScreen

@Composable
fun NavGraph(startDestination: Screens = Screens.SplashScreen) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable<Screens.SplashScreen>{
            SplashScreen(
                navigateToAuth = {
                    navController.navigate(Screens.AuthScreen){
                        popUpTo<Screens.SplashScreen>{
                            inclusive = true
                        }
                    }
                },
                navigateToHome = {
                    navController.navigate(Screens.HomeGraph){
                        popUpTo<Screens.AuthScreen>{
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Screens.AuthScreen>{
            AuthScreen(
                navigateToHome = {
                    navController.navigate(Screens.HomeGraph){
                        popUpTo(Screens.AuthScreen){
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Screens.HomeGraph>{
            HomeScreen()
        }
    }
}