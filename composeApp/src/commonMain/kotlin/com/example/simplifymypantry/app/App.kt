package com.example.simplifymypantry.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.simplifymypantry.login.presentation.LoginScreen
import com.example.simplifymypantry.core.LightColors
import com.example.simplifymypantry.core.customTypography
import com.example.simplifymypantry.core.customShapes
import androidx.navigation.compose.rememberNavController

@Composable
fun App() {
    MaterialTheme(
        colorScheme = LightColors,
        typography = customTypography,
        shapes = customShapes
    ){
        //val navController = rememberNavController()
        LoginScreen()
    }
}