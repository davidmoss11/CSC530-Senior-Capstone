package com.example.simplifymypantry.app

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object AppGraph: Route

    @Serializable
    data object LoginPage: Route

    @Serializable
    data object HomeScreen: Route

    @Serializable
    data object CreateAccount: Route
}