package com.example.simplifymypantry.recipe.presentation

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf

data class Recipe(
    val id: String,
    val name: String,
    val ingredients: List<String>,
    val instructions: String
)

class RecipeScreenViewModel : ViewModel() {
    //Mock Data, should be removed later
    private val _recipes = mutableStateListOf(
        Recipe("1", "Spaghetti Carbonara", listOf("Pasta", "Eggs", "Bacon", "Cheese"), "Boil pasta..."),
        Recipe("2", "Chicken Tacos", listOf("Chicken", "Tortillas", "Salsa"), "Cook chicken..."),
    )
    val recipes: List<Recipe> = _recipes

    fun deleteRecipe(recipe: Recipe) {
        _recipes.remove(recipe)
    }
}
