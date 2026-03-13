package com.example.simplifymypantry.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.simplifymypantry.account.presentation.LoginScreen
import com.example.simplifymypantry.home.presentation.HomeScreen
import com.example.simplifymypantry.account.presentation.CreateAccount
import com.example.simplifymypantry.account.presentation.ViewAccount
import com.example.simplifymypantry.core.LightColors
import com.example.simplifymypantry.core.customTypography
import com.example.simplifymypantry.core.customShapes
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.simplifymypantry.account.presentation.LoginViewModel
import com.example.simplifymypantry.home.presentation.HomeScreenViewModel
import com.example.simplifymypantry.account.presentation.CreateAccountViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplifymypantry.account.presentation.ViewAccountViewModel
import com.example.simplifymypantry.pantry.presentation.PantryScreen
import com.example.simplifymypantry.pantry.presentation.PantryViewModel
import com.example.simplifymypantry.recipe.presentation.RecipeScreen
import com.example.simplifymypantry.recipe.presentation.RecipeViewModel


@Composable
fun App() {
    MaterialTheme(
        colorScheme = LightColors,
        typography = customTypography,
        shapes = customShapes
    ){
        val navController = rememberNavController()

         val pantryViewModel = viewModel<PantryViewModel>()
        val recipeViewModel = viewModel<RecipeViewModel>()

        NavHost(
            navController = navController,
            startDestination = Route.AppGraph
        ){
             navigation<Route.AppGraph>(
                 startDestination = Route.LoginPage
             ){
                 composable<Route.LoginPage>(

                 ){
                     val loginViewModel = viewModel<LoginViewModel>()
                     LoginScreen(
                         viewModel = loginViewModel,
                         onCreateAccount = { navController.navigate(Route.CreateAccount) },
                         onSkip = { navController.navigate(Route.HomeScreen) }
                     )
                 }

                 composable<Route.CreateAccount>(

                 ){
                    val createAccountViewModel = viewModel<CreateAccountViewModel>()
                     CreateAccount(
                         viewModel = createAccountViewModel,
                         onSignIn = { navController.navigate(Route.LoginPage) }
                     )
                 }

                 composable<Route.HomeScreen>(

                 ){
                     val homeScreenViewModel = viewModel<HomeScreenViewModel>()
                             HomeScreen(
                                 viewModel = homeScreenViewModel,
                                 pantryClick = { navController.navigate(Route.Pantry) },
                                 recipeClick = { navController.navigate(Route.Recipes) },
                                 scanClick = { navController.navigate(Route.Scanner) },
                             )
                 }

                 composable<Route.ViewAccount>(

                 ){
                     val viewAccountViewModel = viewModel<ViewAccountViewModel>()
                     ViewAccount(
                         viewModel = viewAccountViewModel
                     )
                 }

                 composable<Route.Pantry> {
                     PantryScreen(viewModel = pantryViewModel)
                 }

                 composable<Route.Recipes> {
                     RecipeScreen(viewModel = recipeViewModel)
                 }
             }
        }
    }
}
