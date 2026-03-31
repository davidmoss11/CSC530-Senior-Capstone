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
import androidx.navigation.toRoute
import com.example.simplifymypantry.account.presentation.ViewAccountViewModel
import com.example.simplifymypantry.pantry.presentation.PantryScreen
import com.example.simplifymypantry.pantry.presentation.PantryViewModel
import com.example.simplifymypantry.recipe.presentation.CreateRecipeScreen
import com.example.simplifymypantry.recipe.presentation.CreateRecipeScreenViewModel
import com.example.simplifymypantry.recipe.presentation.RecipeDetailScreen
import com.example.simplifymypantry.recipe.presentation.RecipeDetailViewModel
import com.example.simplifymypantry.recipe.presentation.RecipeScreen
import com.example.simplifymypantry.recipe.presentation.RecipeScreenViewModel
import com.example.simplifymypantry.recipe.presentation.RecipeViewModel


@Composable
fun App() {
    MaterialTheme(
        colorScheme = LightColors,
        typography = customTypography,
        shapes = customShapes
    ){
        val recipeScreenViewModel = viewModel<RecipeScreenViewModel>()
        val navController = rememberNavController()

         val pantryViewModel = viewModel<PantryViewModel>()
        val recipeViewModel = viewModel<RecipeViewModel>()

        NavHost(
            navController = navController,
            startDestination = Route.AppGraph
        ) {
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
                         onSignIn = { navController.navigate(Route.LoginPage) },
                         onSkip = { navController.navigate(Route.HomeScreen) }
                     )
                 }

                 composable<Route.HomeScreen>(

                 ){
                     val homeScreenViewModel = viewModel<HomeScreenViewModel>()
                             HomeScreen(
                                 viewModel = homeScreenViewModel,
                                 navController = navController,
                                 pantryClick = { navController.navigate(Route.Pantry) },
                                 recipeClick = { navController.navigate(Route.Recipes) },
                                 scanClick = { navController.navigate(Route.Scanner) },
                             )
                 }

                 composable<Route.ViewAccount>(

                 ){
                     val viewAccountViewModel = viewModel<ViewAccountViewModel>()
                     ViewAccount(
                         viewModel = viewAccountViewModel,
                         navController = navController
                     )
                 }

                 composable<Route.CreateRecipe>(

                 ){
                     val createRecipeViewModel = viewModel<CreateRecipeScreenViewModel>()
                     CreateRecipeScreen(
                         viewModel = createRecipeViewModel,
                         navController = navController
                     )
                 }

                 composable<Route.Recipes>(

                 ){
                     val recipeScreenViewModel = viewModel<RecipeScreenViewModel>()
                     RecipeScreen(
                         onCreateRecipeClick = { navController.navigate(Route.CreateRecipe) },
                         viewModel = recipeScreenViewModel,
                         navController = navController
                     )
                 }

                 composable<Route.RecipeDetails>(

                 ){
                     backStackEntry -> val route : Route.RecipeDetails = backStackEntry.toRoute()
                     val recipeViewModel = viewModel {
                         RecipeDetailViewModel(route.recipeId, recipeScreenViewModel)
                     }

                     RecipeDetailScreen(
                         viewModel = recipeViewModel,
                         navController = navController
                     )
                 }

                 composable<Route.Pantry> {
                     PantryScreen(viewModel = pantryViewModel)
                 }

                 composable<Route.Recipes> {
                     RecipeScreen(
                         viewModel = recipeScreenViewModel,
                         navController = navController,
                         onCreateRecipeClick = {} , //unsure, needs to be filled in later
                         )
                 }
             }
        }
    }
}
