package com.example.simplifymypantry.pantry.presentation

import androidx.lifecycle.ViewModel
import com.example.simplifymypantry.pantry.domain.PantryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class PantryViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<PantryItem>>(emptyList())
    val items: StateFlow<List<PantryItem>> = _items.asStateFlow()

    private val _filterQuery = MutableStateFlow("")
    val filterQuery: StateFlow<String> = _filterQuery.asStateFlow()

    // Filtered items based on search/filter query
    val filteredItems = combine(_items, _filterQuery) { items, query ->
        if (query.isBlank()) {
            items
        } else {
            items.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true) ||
                it.dietInfo.contains(query, ignoreCase = true) ||
                it.expirationDate.contains(query, ignoreCase = true)
            }
        }
    }

    fun updateFilter(query: String) {
        _filterQuery.value = query
    }

    fun addItem(
        name: String,
        quantity: String = "",
        category: String = "",
        expiry: String = "",
        diet: String = "",
        notes: String = ""
    ) {
        if (name.isBlank()) return

        val newItem = PantryItem(
            id = (_items.value.size + 1).toString(),
            name = name,
            quantity = quantity,
            category = category,
            expirationDate = expiry,
            dietInfo = diet,
            notes = notes
        )
        _items.value += newItem
    }
}
