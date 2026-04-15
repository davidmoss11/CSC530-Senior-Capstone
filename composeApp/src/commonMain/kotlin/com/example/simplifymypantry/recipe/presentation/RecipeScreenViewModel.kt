package com.example.simplifymypantry.recipe.presentation

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import com.example.simplifymypantry.recipe.domain.Recipe
import com.example.simplifymypantry.pantry.domain.PantryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class RecipeScreenViewModel : ViewModel() {
    private val _recipes = mutableStateListOf<Recipe>(
        Recipe("1", "Pasta", listOf("Pasta", "Tomato"), "Boil pasta.", "Italian", isOfficial = true, price = 5.0, dietaryRestrictions = listOf("Vegetarian"), rating = 4.5, mealType = "Dinner"),
        Recipe("2", "Salad", listOf("Lettuce", "Cucumber"), "Mix it.", "Healthy", isOfficial = true, price = 3.0, dietaryRestrictions = listOf("Vegan"), rating = 4.0, mealType = "Lunch")
    )
    val recipes: List<Recipe> = _recipes

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _maxPrice = MutableStateFlow(100.0)
    val maxPrice = _maxPrice.asStateFlow()

    private val _dietFilter = MutableStateFlow("")
    val dietFilter = _dietFilter.asStateFlow()

    private val _typeFilter = MutableStateFlow("")
    val typeFilter = _typeFilter.asStateFlow()

    private val _minRating = MutableStateFlow(0.0)
    val minRating = _minRating.asStateFlow()

    private val _onlyPantry = MutableStateFlow(false)
    val onlyPantry = _onlyPantry.asStateFlow()

    fun getFilteredRecipes(pantryItems: List<PantryItem>) = combine(
        MutableStateFlow(_recipes.toList()), _searchQuery, _maxPrice, _dietFilter, _typeFilter, _minRating, _onlyPantry
    ) { args: Array<Any> ->
        val recipes = args[0] as List<Recipe>
        val query = args[1] as String
        val price = args[2] as Double
        val diet = args[3] as String
        val type = args[4] as String
        val rating = args[5] as Double
        val onlyPantry = args[6] as Boolean

        recipes.filter { recipe ->
            val matchesQuery = recipe.name.contains(query, ignoreCase = true)
            val matchesPrice = recipe.price <= price
            val matchesDiet = diet.isEmpty() || recipe.dietaryRestrictions.any { it.contains(diet, ignoreCase = true) }
            val matchesType = type.isEmpty() || recipe.mealType.contains(type, ignoreCase = true)
            val matchesRating = recipe.rating >= rating
            
            val matchesPantry = if (onlyPantry) {
                val pantryNames = pantryItems.map { it.name.lowercase() }
                recipe.ingredients.all { ingredient -> 
                    pantryNames.any { it.contains(ingredient.lowercase()) }
                }
            } else true

            matchesQuery && matchesPrice && matchesDiet && matchesType && matchesRating && matchesPantry
        }
    }

    fun updateSearch(query: String) { _searchQuery.value = query }
    fun updatePrice(price: Double) { _maxPrice.value = price }
    fun updateDiet(diet: String) { _dietFilter.value = diet }
    fun updateType(type: String) { _typeFilter.value = type }
    fun updateRating(rating: Double) { _minRating.value = rating }
    fun togglePantryFilter() { _onlyPantry.value = !_onlyPantry.value }

    fun addRecipe(recipe: Recipe) {
        _recipes.add(recipe)
    }

    fun deleteRecipe(recipe: Recipe) {
        _recipes.remove(recipe)
    }

    fun toggleSaveRecipe(id: String) {
        val index = _recipes.indexOfFirst { it.id == id }
        if (index != -1) {
            val recipe = _recipes[index]
            _recipes[index] = recipe.copy(isSaved = !recipe.isSaved)
        }
    }

    fun addReview(id: String, review: String) {
        val index = _recipes.indexOfFirst { it.id == id }
        if (index != -1) {
            val recipe = _recipes[index]
            _recipes[index] = recipe.copy(reviews = recipe.reviews + review)
        }
    }
}
