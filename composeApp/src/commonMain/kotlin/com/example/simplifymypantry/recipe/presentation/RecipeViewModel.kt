package com.example.simplifymypantry.recipe.presentation

import androidx.lifecycle.ViewModel
import com.example.simplifymypantry.recipe.domain.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeViewModel : ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    fun addRecipe(title: String, ingredients: String, instructions: String, category: String) {
        val newRecipe = Recipe(
            id = (recipes.value.size + 1).toString(),
            title = title,
            ingredients = ingredients.split(",").map { it.trim() },
            instructions = instructions,
            category = category
        )
        _recipes.value += newRecipe
    }
}
