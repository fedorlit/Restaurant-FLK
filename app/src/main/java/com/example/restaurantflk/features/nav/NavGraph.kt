package com.example.restaurantflk.features.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.restaurantflk.features.admin_panel.AdminPanelScreen
import com.example.restaurantflk.features.admin_panel.manage_product.ManageProductScreen
import com.example.restaurantflk.features.splash.SplashScreen
import com.example.restaurantflk.features.auth.AuthScreen
import com.example.restaurantflk.features.home.HomeScreen
import com.example.restaurantflk.features.profile.ProfileScreen

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
            HomeScreen(
                navigateToAuth = {
                    navController.navigate(Screens.AuthScreen){
                        popUpTo(Screens.HomeGraph){
                            inclusive = true
                        }
                    }
                },
                navigateToProfile = {
                    navController.navigate(Screens.Profile)
                },
                navigateToAdminPanel = {
                    navController.navigate(Screens.AdminPanel)
                }
            )
        }

        composable<Screens.Profile>{
            ProfileScreen(
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<Screens.AdminPanel>{
            AdminPanelScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToManageProduct = { id ->
                    navController.navigate(Screens.ManageProduct(id=id))
                }
            )
        }

        composable<Screens.ManageProduct>{
            val id = it.toRoute<Screens.ManageProduct>().id
            ManageProductScreen(
                id = id,
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

    }
}