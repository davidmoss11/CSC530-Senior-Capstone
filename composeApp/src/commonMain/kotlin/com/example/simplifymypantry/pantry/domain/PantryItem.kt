package com.example.simplifymypantry.pantry.domain

import com.example.simplifymypantry.pantry.data.PantryItemEntity
import com.example.simplifymypantry.scanner.data.Ingredient
import com.example.simplifymypantry.scanner.data.Nutriments
import com.example.simplifymypantry.scanner.data.SelectedImages
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class PantryItem(
    val id: String = "",
    val productType: String? = null,
    val productName: String? = null,
    val productQuantity: Double? = null,
    val productQuantityUnit: String? = null,
    val quantity: String? = null,
    val foodGroups: String? = null,
    val brandsTags: List<String>? = null,
    val categoriesTags: List<String>? = null,
    val labelsTags: List<String>? = null,
    val selectedImages: SelectedImages? = null,
    val allergensTags: List<String>? = null,
    val ingredients: List<Ingredient>? = null,
    val nutriments: Nutriments? = null,
    val allergensFromIngredients: String? = null,
    val expirationDate: String? = null,
    val notes: String? = ""
)

fun PantryItemEntity.toPantryItem(): PantryItem {
    return PantryItem(
        id = id.toString(),
        productType = productType,
        productName = productName,
        productQuantity = productQuantity,
        productQuantityUnit = productQuantityUnit,
        quantity = quantity,
        foodGroups = foodGroups,
        brandsTags = brandsTags?.split(",")?.filter { it.isNotBlank() },
        categoriesTags = categoriesTags?.split(",")?.filter { it.isNotBlank() },
        labelsTags = labelsTags?.split(",")?.filter { it.isNotBlank() },
        selectedImages = selectedImages?.let {
            Json.decodeFromString(it)
        },
        allergensTags = allergensTags?.split(",")?.filter { it.isNotBlank() },
        ingredients = ingredients?.let {
            Json.decodeFromString(it)
        },
        nutriments = nutriments?.let {
            Json.decodeFromString(it)
        },
        allergensFromIngredients = allergensFromIngredients,
        expirationDate = expirationDate,
        notes = notes
    )
}

fun PantryItem.toPantryItemEntity(): PantryItemEntity {
    return PantryItemEntity(
        id = id.toLongOrNull() ?: 0L, // 0 lets AUTOINCREMENT handle new inserts
        productType = productType,
        productName = productName ?: "",
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
        selectedImages = selectedImages?.let {
            Json.encodeToString(it)
        },
        ingredients = ingredients?.let {
            Json.encodeToString(it)
        },
        nutriments = nutriments?.let {
            Json.encodeToString(it)
        },
        notes = notes
    )
}
