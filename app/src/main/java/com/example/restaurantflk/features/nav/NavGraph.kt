package com.example.restaurantflk.features.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.restaurantflk.features.admin_panel.AdminPanelScreen
import com.example.restaurantflk.features.admin_panel.manage_product.ManageProductScreen
import com.example.restaurantflk.features.splash.SplashScreen
import com.example.restaurantflk.features.auth.AuthScreen
import com.example.restaurantflk.features.home.HomeScreen
import com.example.restaurantflk.features.product_details.ProductDetailsScreen
import com.example.restaurantflk.features.profile.ProfileScreen

const val HOME_TAB_KEY = "HOME_TAB_KEY"
private fun NavController.setHomeTab(tab:HomeTab){
    try {
        val homeEntry = getBackStackEntry<Screens.HomeGraph>()
        homeEntry.savedStateHandle[HOME_TAB_KEY] = tab
    }catch (e: IllegalArgumentException){}
}

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
                    navController.navigate(Screens.HomeGraph()){
                        popUpTo<Screens.SplashScreen>{
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

        composable<Screens.HomeGraph>{ entry ->
            val args = entry.toRoute<Screens.HomeGraph>()
            val requestedTabFlow = entry.savedStateHandle.getStateFlow(HOME_TAB_KEY,args.start)
            val requestStateTab by requestedTabFlow.collectAsState()

            HomeScreen(
                startTab = requestStateTab,
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
                },
                navigateToDetails = { productId ->
                    navController.navigate(Screens.DetailsScreen(id = productId))
                },
                navigateToCheckout = {amount ->
                    navController.navigate(Screens.CartScreen(amount = amount))
                },
                navigateToMenu = {}
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

        composable<Screens.DetailsScreen> {
            ProductDetailsScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToCart = {
                    navController.setHomeTab(HomeTab.Cart)
                    navController.popBackStack()
                }
            )
        }
    }
}