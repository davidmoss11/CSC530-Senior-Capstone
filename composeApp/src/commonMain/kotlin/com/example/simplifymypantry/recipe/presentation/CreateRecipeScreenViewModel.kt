package com.example.simplifymypantry.recipe.presentation

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

class CreateRecipeScreenViewModel : ViewModel () {

    var recipeName by mutableStateOf("")
    var ingredients by mutableStateOf("")
    var instructions by mutableStateOf("")

    var showDialog by mutableStateOf(false)
    var dialogMessage by mutableStateOf("")

    fun saveRecipeClicked() {
        if (recipeName.isBlank() || ingredients.isBlank() || instructions.isBlank()) {
            dialogMessage = "Please fill in all fields before saving."
            showDialog = true
            return
        }
        //TODO: add logic to save the recipe here
        dialogMessage = "Recipe '$recipeName' saved successfully!"
        showDialog = true
    }

    private fun clearFields() {
        recipeName = ""
        ingredients = ""
        instructions = ""
    }
}