package com.example.simplifymypantry.app

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
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
import com.example.simplifymypantry.account.data.AccountDatabase
import com.example.simplifymypantry.account.data.AccountRepository
import com.example.simplifymypantry.account.data.DeleteUserUseCase
import com.example.simplifymypantry.account.data.EditUserUseCase
import com.example.simplifymypantry.account.data.GetUserUseCase
import com.example.simplifymypantry.account.data.LoginUserUseCase
import com.example.simplifymypantry.account.data.RegisterUserUseCase
import com.example.simplifymypantry.account.presentation.HouseholdScreen
import com.example.simplifymypantry.account.presentation.HouseholdViewModel
import com.example.simplifymypantry.account.presentation.ViewAccountViewModel
import com.example.simplifymypantry.pantry.presentation.PantryScreen
import com.example.simplifymypantry.pantry.presentation.PantryViewModel
import com.example.simplifymypantry.recipe.presentation.CreateRecipeScreen
import com.example.simplifymypantry.recipe.presentation.CreateRecipeScreenViewModel
import com.example.simplifymypantry.recipe.presentation.RecipeDetailScreen
import com.example.simplifymypantry.recipe.presentation.RecipeDetailViewModel
import com.example.simplifymypantry.recipe.presentation.RecipeScreen
import com.example.simplifymypantry.recipe.presentation.RecipeScreenViewModel
import com.example.simplifymypantry.account.data.SessionManager
import com.example.simplifymypantry.pantry.data.PantryDatabase
import com.example.simplifymypantry.scanner.data.ImageSaver
import com.example.simplifymypantry.scanner.data.OpenFoodFactsAPI
import com.example.simplifymypantry.scanner.data.PantryItemCache
import com.example.simplifymypantry.scanner.data.Scanner
import com.example.simplifymypantry.scanner.presentation.ScannerScreen
import com.example.simplifymypantry.scanner.presentation.ScannerScreenViewModel

@Composable
fun App(
    database: PantryDatabase,
    accountDatabase: AccountDatabase,
    scannerDatabase: PantryItemCache,
    scanner: Scanner,
    imageSaver: ImageSaver
){
    // Properties might not be generated yet if sync is incomplete, but these are standard names
    val queries = database.pantryDatabaseQueries

    MaterialTheme(
        colorScheme = LightColors,
        typography = customTypography,
        shapes = customShapes
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyle.current.copy(color = Color.Black),
            LocalContentColor provides Color.Black
        ) {
            val pantryViewModel = viewModel { PantryViewModel(queries) }
            val sharedRecipeViewModel = viewModel<RecipeScreenViewModel>()
            val navController = rememberNavController()

            val openFoodFactsApi = remember { OpenFoodFactsAPI() }
            val scannerScreenViewModel = viewModel {
                ScannerScreenViewModel(scanner, openFoodFactsApi, scannerDatabase, database,imageSaver)
            }

            val sessionManager = remember { SessionManager(accountDatabase) }

            val apiService = remember { AccountApiService() }
            val repository = remember { AccountRepository(apiService) }
            val editUserUseCase = remember { EditUserUseCase(repository) }
            val registerUserUseCase = remember { RegisterUserUseCase(repository) }
            val getUserUseCase = remember { GetUserUseCase(repository) }
            val deleteUserUseCase = remember { DeleteUserUseCase(repository) }
            val loginUserUseCase = remember { LoginUserUseCase(repository) }

            val session = sessionManager.getSession()

            val startDestination = if (session != null) {
                Route.HomeScreen
            }
            else {
                Route.LoginPage
            }

            NavHost(
                navController = navController,
                startDestination = Route.AppGraph
            ) {
                navigation<Route.AppGraph>(
                    startDestination = startDestination
                ) {
                    composable<Route.LoginPage> {
                        val loginViewModel =
                            viewModel { LoginViewModel(loginUserUseCase, sessionManager) }
                        LoginScreen(
                            viewModel = loginViewModel,
                            onCreateAccount = { navController.navigate(Route.CreateAccount) },
                            onSkip = { navController.navigate(Route.HomeScreen) }
                        )
                    }

                    composable<Route.CreateAccount> {
                        val createAccountViewModel = viewModel {
                            CreateAccountViewModel(
                                registerUserUseCase,
                                sessionManager
                            )
                        }
                        CreateAccount(
                            viewModel = createAccountViewModel,
                            onSignIn = { navController.navigate(Route.LoginPage) },
                            onSkip = { navController.navigate(Route.HomeScreen) }
                        )
                    }

                    composable<Route.HomeScreen> {
                        val homeScreenViewModel = viewModel<HomeScreenViewModel>()
                        HomeScreen(
                            viewModel = homeScreenViewModel,
                            navController = navController,
                            pantryClick = { navController.navigate(Route.Pantry) },
                            recipeClick = { navController.navigate(Route.Recipes) },
                            scanClick = { navController.navigate(Route.Scanner) },
                        )
                    }

                    composable<Route.ViewAccount> {
                        val viewAccountViewModel = viewModel {
                            ViewAccountViewModel(
                                deleteUserUseCase,
                                editUserUseCase,
                                getUserUseCase,
                                sessionManager
                            )
                        }
                        ViewAccount(
                            viewModel = viewAccountViewModel,
                            navController = navController,
                            token = "tempToken"
                        )
                    }

                    composable<Route.CreateRecipe> {
                        val createRecipeViewModel = viewModel {
                            CreateRecipeScreenViewModel(sharedRecipeViewModel)
                        }
                        CreateRecipeScreen(
                            viewModel = createRecipeViewModel,
                            navController = navController
                        )
                    }

                    composable<Route.Recipes> {
                        RecipeScreen(
                            onCreateRecipeClick = { navController.navigate(Route.CreateRecipe) },
                            viewModel = sharedRecipeViewModel,
                            pantryViewModel = pantryViewModel,
                            navController = navController
                        )
                    }

                    composable<Route.RecipeDetails> { backStackEntry ->
                        val route: Route.RecipeDetails = backStackEntry.toRoute()
                        val recipeDetailViewModel = viewModel {
                            RecipeDetailViewModel(route.recipeId, sharedRecipeViewModel)
                        }

                        RecipeDetailScreen(
                            viewModel = recipeDetailViewModel,
                            navController = navController
                        )
                    }

                    composable<Route.Household> {
                        val householdViewModel = viewModel<HouseholdViewModel>()
                        HouseholdScreen(
                            viewModel = householdViewModel,
                            navController = navController
                        )
                    }

                    composable<Route.Pantry> {
                        PantryScreen(viewModel = pantryViewModel, navController = navController)
                    }

                    composable<Route.Scanner> {

                        ScannerScreen(
                            viewModel = scannerScreenViewModel,
                            returnHome = { navController.navigate(Route.HomeScreen) }
                        )
                    }
                }
            }
        }
    }
}
