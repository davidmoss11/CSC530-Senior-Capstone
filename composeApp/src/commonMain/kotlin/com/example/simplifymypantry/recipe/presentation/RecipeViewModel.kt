package com.example.simplifymypantry.recipe.presentation

import androidx.lifecycle.ViewModel
import com.example.simplifymypantry.recipe.domain.Recipe
import com.example.simplifymypantry.pantry.domain.PantryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class RecipeViewModel : ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(
        listOf(
            Recipe("1", "Pasta", listOf("Pasta", "Tomato"), "Boil pasta.", "Italian", true, 5.0, listOf("Vegetarian"), 4.5, "Dinner"),
            Recipe("2", "Salad", listOf("Lettuce", "Cucumber"), "Mix it.", "Healthy", true, 3.0, listOf("Vegan"), 4.0, "Lunch")
        )
    )
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

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
        _recipes, _searchQuery, _maxPrice, _dietFilter, _typeFilter, _minRating, _onlyPantry
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
                val pantryNames = pantryItems.map { it.productName?.lowercase() }
                recipe.ingredients.all { ingredient -> 
                    pantryNames.any { it?.contains(ingredient.lowercase()) == null }
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

    fun addRecipe(name: String, ingredients: String, instructions: String, category: String) {
        val newRecipe = Recipe(
            id = (_recipes.value.size + 1).toString(),
            name = name,
            ingredients = ingredients.split(",").map { it.trim() },
            instructions = instructions,
            category = category,
            isOfficial = false
        )
        _recipes.value += newRecipe
    }

    fun toggleSaveRecipe(id: String) {
        _recipes.value = _recipes.value.map {
            if (it.id == id) it.copy(isSaved = !it.isSaved) else it
        }
    }

    fun addReview(id: String, review: String) {
        _recipes.value = _recipes.value.map {
            if (it.id == id) it.copy(reviews = it.reviews + review) else it
        }
    }
}
