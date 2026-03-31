package com.example.simplifymypantry.recipe.presentation

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.example.simplifymypantry.recipe.domain.Recipe

class CreateRecipeScreenViewModel(private val mainViewModel: RecipeScreenViewModel) : ViewModel () {

    var recipeName by mutableStateOf("")
    var ingredients by mutableStateOf("")
    var instructions by mutableStateOf("")
    var isPublic by mutableStateOf(false)

    var showDialog by mutableStateOf(false)
    var dialogMessage by mutableStateOf("")

    fun saveRecipeClicked() {
        if (recipeName.isBlank() || ingredients.isBlank()) {
            dialogMessage = "Please fill in Name and Ingredients."
            showDialog = true
            return
        }

        if (isPublic && instructions.isBlank()) {
            dialogMessage = "Instructions are required for public recipes."
            showDialog = true
            return
        }

        val newRecipe = Recipe(
            id = (mainViewModel.recipes.size + 1).toString(),
            name = recipeName,
            ingredients = ingredients.split(",").map { it.trim() },
            instructions = instructions,
            isPublic = isPublic
        )

        mainViewModel.addRecipe(newRecipe)

        dialogMessage = "Recipe '$recipeName' saved successfully!"
        showDialog = true
    }

    private fun clearFields() {
        recipeName = ""
        ingredients = ""
        instructions = ""
        isPublic = false
    }
}