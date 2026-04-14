package com.example.simplifymypantry.recipe.presentation

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import com.example.simplifymypantry.recipe.domain.Recipe

class RecipeScreenViewModel : ViewModel() {
    private val _recipes = mutableStateListOf<Recipe>(
        Recipe("1", "Spaghetti Carbonara", listOf("Pasta", "Eggs", "Bacon", "Cheese"), "Boil pasta..."),
        Recipe("2", "Chicken Tacos", listOf("Chicken", "Tortillas", "Salsa"), "Cook chicken..."),
    )
    val recipes: List<Recipe> = _recipes

    fun addRecipe(recipe: Recipe) {
        _recipes.add(recipe)
    }

    fun deleteRecipe(recipe: Recipe) {
        _recipes.remove(recipe)
    }
}
