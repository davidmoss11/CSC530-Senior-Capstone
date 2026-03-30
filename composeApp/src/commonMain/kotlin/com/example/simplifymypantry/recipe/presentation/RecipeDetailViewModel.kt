package com.example.simplifymypantry.recipe.presentation

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.simplifymypantry.recipe.domain.Recipe
import com.example.simplifymypantry.recipe.domain.Comment

class RecipeDetailViewModel(val recipeId: String, private val mainViewModel: RecipeScreenViewModel) : ViewModel() {

    private var recipe = mainViewModel.recipes.find { it.id == recipeId }

    var name by mutableStateOf(recipe?.name ?: "")
    var ingredients by mutableStateOf(recipe?.ingredients?.joinToString("\n") ?: "")
    var instructions by mutableStateOf(recipe?.instructions ?: "")
    var isSaved by mutableStateOf(recipe?.isSaved ?: false)
    var comments = mutableStateListOf<Comment>().apply {
        recipe?.comments?.let { addAll(it) }
    }

    fun updateRecipe() {
        val updatedRecipe = recipe?.copy(
            name = name,
            ingredients = ingredients.split("\n").map { it.trim() },
            instructions = instructions,
            isSaved = isSaved,
            comments = comments.toList()
        )
        updatedRecipe?.let { mainViewModel.addRecipe(it) }
    }

    fun toggleSave() {
        isSaved = !isSaved
    }

    fun shareRecipe() {
        println("Sharing recipe: $name")
    }

    fun addComment(text: String) {
        if (text.isNotBlank()) {
            comments.add(Comment(user = "Me", text = text, rating = 5))
        }
    }

    fun deleteRecipe(onDeleted: () -> Unit) {
        recipe?.let {
            mainViewModel.deleteRecipe(it)
            onDeleted()
        }
    }
}