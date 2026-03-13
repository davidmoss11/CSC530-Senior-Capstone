package com.example.simplifymypantry.recipe.domain

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: String = "",
    val title: String = "",
    val ingredients: List<String> = emptyList(),
    val instructions: String = "",
    val category: String = ""
)
