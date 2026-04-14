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
import com.example.simplifymypantry.account.data.AccountApiService
import com.example.simplifymypantry.account.data.AccountRepository
import com.example.simplifymypantry.account.data.DeleteUserUseCase
import com.example.simplifymypantry.account.data.EditUserUseCase
import com.example.simplifymypantry.account.data.GetUserUseCase
import com.example.simplifymypantry.account.data.LoginUserUseCase
import com.example.simplifymypantry.account.data.RegisterUserUseCase
import com.example.simplifymypantry.account.presentation.HouseholdScreen
import com.example.simplifymypantry.account.presentation.HouseholdViewModel
import com.example.simplifymypantry.account.presentation.ViewAccountViewModel
import com.example.simplifymypantry.pantry.data.DriverFactory
import com.example.simplifymypantry.pantry.presentation.PantryScreen
import com.example.simplifymypantry.pantry.presentation.PantryViewModel
import com.example.simplifymypantry.recipe.presentation.CreateRecipeScreen
import com.example.simplifymypantry.recipe.presentation.CreateRecipeScreenViewModel
import com.example.simplifymypantry.recipe.presentation.RecipeDetailScreen
import com.example.simplifymypantry.recipe.presentation.RecipeDetailViewModel
import com.example.simplifymypantry.recipe.presentation.RecipeScreen
import com.example.simplifymypantry.recipe.presentation.RecipeScreenViewModel
import com.example.simplifymypantry.recipe.presentation.RecipeViewModel
import co.touchlab.kermit.Logger
import co.touchlab.kermit.platformLogWriter
import com.example.simplifymypantry.account.data.AccountDriver
import com.example.simplifymypantry.account.data.SessionManager
import com.example.simplifymypantry.account.data.createAccountDatabase
import com.example.simplifymypantry.pantry.data.createDatabase

fun initAppLogger() {
    Logger.setLogWriters(platformLogWriter())
}

@Composable
fun App(driverFactory: DriverFactory, accountDriver: AccountDriver) {
    val database = remember { createDatabase(driverFactory) }
    val accountDatabase = remember { createAccountDatabase(accountDriver)}
    val queries = database.pantryDatabaseQueries
    val accountDb = accountDatabase.accountDatabaseQueries
    initAppLogger()

    MaterialTheme(
        colorScheme = LightColors,
        typography = customTypography,
        shapes = customShapes
    ){
        // We create this here so it's shared between the List and Create screens
        val pantryViewModel = viewModel { PantryViewModel(queries) }
        val sharedRecipeViewModel = viewModel<RecipeScreenViewModel>()
        val navController = rememberNavController()

        //val pantryViewModel = viewModel<PantryViewModel>()
        val recipeViewModel = viewModel<RecipeViewModel>()
        
        val sessionManager = remember { SessionManager(accountDatabase)}

        //Front End logic and API services
        val apiService = remember { AccountApiService() }
        val repository = remember { AccountRepository(apiService) }
        val editUserUseCase = remember { EditUserUseCase(repository) }
        val registerUserUseCase = remember {RegisterUserUseCase(repository) }
        val getUserUseCase = remember { GetUserUseCase(repository) }
        val deleteUserUseCase = remember { DeleteUserUseCase(repository) }
        val loginUserUseCase = remember { LoginUserUseCase(repository) }

        NavHost(
            navController = navController,
            startDestination = Route.AppGraph
        ) {
             navigation<Route.AppGraph>(
                 startDestination = Route.LoginPage
             ){
                 composable<Route.LoginPage>{
                     val loginViewModel = viewModel { LoginViewModel(loginUserUseCase) }
                     LoginScreen(
                         viewModel = loginViewModel,
                         onCreateAccount = { navController.navigate(Route.CreateAccount) },
                         onSkip = { navController.navigate(Route.HomeScreen) }
                     )
                 }

                 composable<Route.CreateAccount>{
                    val createAccountViewModel = viewModel { CreateAccountViewModel(registerUserUseCase, sessionManager)}
                     CreateAccount(
                         viewModel = createAccountViewModel,
                         onSignIn = { navController.navigate(Route.LoginPage) },
                         onSkip = { navController.navigate(Route.HomeScreen) }
                     )
                 }

                 composable<Route.HomeScreen>{
                     val homeScreenViewModel = viewModel<HomeScreenViewModel>()
                             HomeScreen(
                                 viewModel = homeScreenViewModel,
                                 navController = navController,
                                 pantryClick = { navController.navigate(Route.Pantry) },
                                 recipeClick = { navController.navigate(Route.Recipes) },
                                 scanClick = { navController.navigate(Route.Scanner) },
                             )
                 }

                 composable<Route.ViewAccount>{
                     val viewAccountViewModel = viewModel { ViewAccountViewModel(deleteUserUseCase, editUserUseCase, getUserUseCase, sessionManager) }
                     ViewAccount(
                         viewModel = viewAccountViewModel,
                         navController = navController,
                         token = "tempToken"
                     )
                 }

                 composable<Route.CreateRecipe>{
                     // Pass the shared viewModel so we can add the recipe to the list
                     val createRecipeViewModel = viewModel {
                         CreateRecipeScreenViewModel(sharedRecipeViewModel)
                     }
                     CreateRecipeScreen(
                         viewModel = createRecipeViewModel,
                         navController = navController
                     )
                 }

                 composable<Route.Recipes>{
                     RecipeScreen(
                         onCreateRecipeClick = { navController.navigate(Route.CreateRecipe) },
                         viewModel = recipeViewModel,
                         pantryViewModel = pantryViewModel,
                         navController = navController
                     )
                 }

                 composable<Route.RecipeDetails>{ backStackEntry -> 
                     val route : Route.RecipeDetails = backStackEntry.toRoute()
                     val recipeViewModel = viewModel {
                         RecipeDetailViewModel(route.recipeId, sharedRecipeViewModel)
                     }

                     RecipeDetailScreen(
                         viewModel = recipeViewModel,
                         navController = navController
                     )
                 }

                 composable<Route.Household>{
                     val householdViewModel = viewModel<HouseholdViewModel>()
                     HouseholdScreen(
                         viewModel = householdViewModel,
                         navController = navController
                     )
                 }

                 composable<Route.Pantry> {
                     PantryScreen(viewModel = pantryViewModel, navController = navController)
                 }
             }
        }
    }
}
