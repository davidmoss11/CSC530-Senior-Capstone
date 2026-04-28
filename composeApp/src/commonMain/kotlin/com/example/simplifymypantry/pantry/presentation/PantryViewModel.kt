package com.example.simplifymypantry.pantry.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.simplifymypantry.pantry.data.PantryDatabaseQueries
import com.example.simplifymypantry.pantry.domain.PantryItem
import com.example.simplifymypantry.pantry.domain.toPantryItem
import com.example.simplifymypantry.scanner.data.Ingredient
import com.example.simplifymypantry.scanner.data.Nutriments
import com.example.simplifymypantry.scanner.data.SelectedImages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class PantryViewModel(private val queries: PantryDatabaseQueries) : ViewModel() {

    // Observe the database as a Flow and map to your domain PantryItem
    val items: StateFlow<List<PantryItem>> = queries.getAllItems()
        .asFlow()
        .mapToList(Dispatchers.Default)
        .map { entities ->
            entities.map { it.toPantryItem() }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _filterQuery = MutableStateFlow("")
    val filterQuery: StateFlow<String> = _filterQuery.asStateFlow()

    val filteredItems = combine(items, _filterQuery) { items, query ->
        if (query.isBlank()) {
            items
        } else {
            items.filter { item ->
                item.productName?.contains(query, ignoreCase = true) == true ||
                        item.productType?.contains(query, ignoreCase = true) == true ||
                        item.quantity?.contains(query, ignoreCase = true) == true ||
                        item.foodGroups?.contains(query, ignoreCase = true) == true ||
                        item.brandsTags?.any { it.contains(query, ignoreCase = true) } == true ||
                        item.categoriesTags?.any { it.contains(query, ignoreCase = true) } == true ||
                        item.labelsTags?.any { it.contains(query, ignoreCase = true) } == true ||
                        item.allergensTags?.any { it.contains(query, ignoreCase = true) } == true ||
                        item.expirationDate?.contains(query, ignoreCase = true) == true ||
                        item.notes?.contains(query, ignoreCase = true) == true
            }
        }
    }

    fun updateFilter(query: String) {
        _filterQuery.value = query
    }

    fun addItem(
        id: String = "",
        productType: String? = null,
        productName: String? = null,
        productQuantity: Double? = null,
        productQuantityUnit: String? = null,
        quantity: String? = null,
        foodGroups: String? = null,
        brandsTags: List<String>? = null,
        categoriesTags: List<String>? = null,
        labelsTags: List<String>? = null,
        selectedImages: SelectedImages? = null,
        allergensTags: List<String>? = null,
        ingredients: List<Ingredient>? = null,
        nutriments: Nutriments? = null,
        allergensFromIngredients: String? = null,
        expirationDate: String? = null,
        notes: String? = ""
    ) {
        if (productName.isNullOrBlank()) return

        viewModelScope.launch {
            queries.insertItem(
                productType = productType,
                productName = productName,
                productQuantity = productQuantity,
                productQuantityUnit = productQuantityUnit,
                quantity = quantity,
                foodGroups = foodGroups,
                brandsTags = brandsTags?.joinToString(","),
                categoriesTags = categoriesTags?.joinToString(","),
                labelsTags = labelsTags?.joinToString(","),
                allergensTags = allergensTags?.joinToString(","),
                allergensFromIngredients = allergensFromIngredients,
                expirationDate = expirationDate,
                selectedImages = selectedImages?.let { Json.encodeToString(it) },
                ingredients = ingredients?.let { Json.encodeToString(it) },
                nutriments = nutriments?.let { Json.encodeToString(it) },
                notes = notes ?: ""
            )
        }
    }

    fun updateItem(
        id: String,
        productType: String? = "",
        productName: String? = "",
        productQuantity: Double? = 0.0,
        productQuantityUnit: String? = "",
        quantity: String? = "",
        foodGroups: String? = "",
        brandsTags: List<String>? = null,
        categoriesTags: List<String>? = null,
        labelsTags: List<String>? = null,
        selectedImages: SelectedImages? = null,
        allergensTags: List<String>? = null,
        ingredients: List<Ingredient>? = null,
        nutriments: Nutriments? = null,
        allergensFromIngredients: String? = "",
        expirationDate: String? = "",
        notes: String? = ""
    ) {
        if (productName.isNullOrBlank()) return

        viewModelScope.launch {
            queries.updateItem(
                id = id.toLong(),
                productType = productType,
                productName = productName,
                productQuantity = productQuantity,
                productQuantityUnit = productQuantityUnit,
                quantity = quantity,
                foodGroups = foodGroups,
                brandsTags = brandsTags?.joinToString(","),
                categoriesTags = categoriesTags?.joinToString(","),
                labelsTags = labelsTags?.joinToString(","),
                allergensTags = allergensTags?.joinToString(","),
                allergensFromIngredients = allergensFromIngredients,
                expirationDate = expirationDate,
                selectedImages = selectedImages?.let { Json.encodeToString(it) },
                ingredients = ingredients?.let { Json.encodeToString(it) },
                nutriments = nutriments?.let { Json.encodeToString(it) },
                notes = notes
            )
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch {
            queries.deleteItem(id)
        }
    }
}
