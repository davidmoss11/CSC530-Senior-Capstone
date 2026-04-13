package com.example.simplifymypantry.recipe.domain

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: String = "",
    val title: String = "",
    val ingredients: List<String> = emptyList(),
    val instructions: String = "",
    val category: String = "",
    val isOfficial: Boolean = false,
    val price: Double = 0.0,
    val dietaryRestrictions: List<String> = emptyList(),
    val rating: Double = 0.0,
    val mealType: String = "",
    val reviews: List<String> = emptyList(),
    val isSaved: Boolean = false
)
