package com.example.simplifymypantry.pantry.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.simplifymypantry.pantry.data.PantryDatabaseQueries
import com.example.simplifymypantry.pantry.domain.PantryItem
import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PantryViewModel(private val queries: PantryDatabaseQueries) : ViewModel() {

    // Observe the database as a Flow and map to your domain PantryItem
    val items: StateFlow<List<PantryItem>> = queries.getAllItems()
        .asFlow()
        .mapToList(Dispatchers.Default) //was Dispatchers.IO, but I kept getting an error, will revert if needed
        .map { entities ->
            entities.map { entity ->
                PantryItem(
                    id = entity.id.toString(),
                    name = entity.name,
                    quantity = entity.quantity,
                    category = entity.category,
                    expirationDate = entity.expirationDate,
                    dietInfo = entity.dietInfo,
                    notes = entity.notes
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _filterQuery = MutableStateFlow("")
    val filterQuery: StateFlow<String> = _filterQuery.asStateFlow()

    val filteredItems = combine(items, _filterQuery) { items, query ->
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
        viewModelScope.launch {
            queries.insertItem(
                name = name,
                quantity = quantity,
                category = category,
                expirationDate = expiry,
                dietInfo = diet,
                notes = notes
            )
        }
    }

    fun updateItem(
        id: Long,
        name: String,
        quantity: String = "",
        category: String = "",
        expiry: String = "",
        diet: String = "",
        notes: String = ""
    ) {
        if (name.isBlank()) return
        viewModelScope.launch {
            queries.updateItem(
                name = name,
                quantity = quantity,
                category = category,
                expirationDate = expiry,
                dietInfo = diet,
                notes = notes,
                id = id
            )
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch {
            queries.deleteItem(id)
        }
    }
}
