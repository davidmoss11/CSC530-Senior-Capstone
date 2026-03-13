package com.example.simplifymypantry.recipe.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RecipeDetailViewModel(val recipeId: String, private val mainViewModel: RecipeScreenViewModel) : ViewModel() {

    private val recipe = mainViewModel.recipes.find { it.id == recipeId }

    var name by mutableStateOf(recipe?.name ?: "")
    var ingredients by mutableStateOf(recipe?.ingredients?.joinToString("\n") ?: "")
    var instructions by mutableStateOf(recipe?.instructions ?: "")

    fun updateRecipe() {
        //TODO: Logic to update recipe in database
    }

    fun deleteRecipe(onDeleted: () -> Unit) {
        recipe?.let {
            mainViewModel.deleteRecipe(it)
            onDeleted()
        }
    }
}