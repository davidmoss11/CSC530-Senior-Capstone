package com.example.simplifymypantry.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.simplifymypantry.login.presentation.LoginScreen
import com.example.simplifymypantry.core.LightColors
import com.example.simplifymypantry.core.customTypography
import com.example.simplifymypantry.core.customShapes
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.simplifymypantry.login.presentation.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun App() {
    MaterialTheme(
        colorScheme = LightColors,
        typography = customTypography,
        shapes = customShapes
    ){
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.AppGraph
        ){
             navigation<Route.AppGraph>(
                 startDestination = Route.LoginPage
             ){
                 composable<Route.LoginPage>(
                     //exitTransition = {},
                     //popEnterTransition = {}
                 ){
                     var loginViewModel = viewModel<LoginViewModel>()
                     LoginScreen(loginViewModel)
                 }
             }


        }
    }
}