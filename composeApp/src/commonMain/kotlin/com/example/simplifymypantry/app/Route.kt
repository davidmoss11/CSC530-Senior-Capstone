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

    @Serializable
    data object Pantry: Route
    //both of these are classes because they must take an object
    @Serializable
    data object Recipes: Route

    @Serializable
    data object Scanner: Route

    @Serializable
    data object ViewAccount: Route
}