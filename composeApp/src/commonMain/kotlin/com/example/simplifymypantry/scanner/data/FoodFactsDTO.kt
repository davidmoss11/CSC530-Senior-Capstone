package com.example.simplifymypantry.scanner.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class ProductResponse(
    val code: String,
    val product: Product? = null
)

@Serializable
data class Product(
    @SerialName("product_type")
    val productType: String? = null,
    @SerialName("product_name")
    val productName: String? = null,
    @SerialName("product_quantity")
    val productQuantity: Double? = null,
    @SerialName("product_quantity_unit")
    val productQuantityUnit: String? = null,
    val quantity: String? = null,
    @SerialName("food_groups")
    val foodGroups: String? = null,
    @SerialName("brand_tags")
    val brandsTags: List<String>? = null,
    @SerialName("categories_tags")
    val categoriesTags: List<String>? = null,
    @SerialName("labels_tags")
    val labelsTags: List<String>? = null,
    @SerialName("selected_images")
    val selectedImages: SelectedImages? = null,
    @SerialName("allergens_tags")
    val allergensTags: List<String>? = null,
    val ingredients: List<Ingredient>? = null,
    val nutriments: Nutriments? = null,
    @SerialName("allergens_from_ingredients")
    val allergensFromIngredients: String? = null,
    @SerialName("expiration_date")
    val expirationDate: String? = null
)

@Serializable
data class SelectedImages(
    val front: ImageSet? = null,
    val ingredients: ImageSet? = null,
    val packaging: ImageSet? = null

)

@Serializable
data class ImageSet(
    val display: Map<String, String>? = null,
    val small: Map<String, String>? = null,
    val thumb: Map<String, String>? = null
)

@Serializable
data class Ingredient(
    val id: String? = null,
    val ingredients: List<Ingredient>? = null,
    val percent: Double? = null,
    val vegan: String? = null,
    val vegetarian: String? = null,
)

@Serializable
data class Nutriments(
    val alcohol: Double? = null,
    val carbohydrates: Double? = null,
    @SerialName("carbohydrates-total")
    val carbohydratesTotal: Double? = null,
    @SerialName("energy_value")
    val energyValue: Double? = null,
    val fat: Double? = null,
    val proteins: Double? = null,
    val salt: Double? = null,
    @SerialName("saturated-fat")
    val saturatedFat: Double? = null,
    val sodium: Double? = null,
    val sugars: Double? = null
)


fun ProductResponse.toScannerEntity(): ScannerItemEntity {
    return ScannerItemEntity(
        code = code,
        productName = product?.productName ?: "",
        productType = product?.productType,
        productQuantity = product?.productQuantity,
        productQuantityUnit = product?.productQuantityUnit,
        quantity = product?.quantity,
        foodGroups = product?.foodGroups,
        brandsTags = product?.brandsTags?.joinToString(","),
        categoriesTags = product?.categoriesTags?.joinToString(","),
        labelsTags = product?.labelsTags?.joinToString(","),
        allergensTags = product?.allergensTags?.joinToString(","),
        allergensFromIngredients = product?.allergensFromIngredients,
        expirationDate = product?.expirationDate,
        selectedImages = Json.encodeToString(product?.selectedImages),
        ingredients = Json.encodeToString(product?.ingredients),
        nutriments = Json.encodeToString(product?.nutriments)
    )
}

fun ScannerItemEntity.toProductResponse(): ProductResponse {
    return ProductResponse(
        code = code,
        product = Product(
            productName = productName,
            productType = productType,
            productQuantity = productQuantity,
            productQuantityUnit = productQuantityUnit,
            quantity = quantity,
            foodGroups = foodGroups,
            brandsTags = brandsTags?.split(",") ?: emptyList(),
            categoriesTags = categoriesTags?.split(",") ?: emptyList(),
            labelsTags = labelsTags?.split(",") ?: emptyList(),
            allergensTags = allergensTags?.split(",") ?: emptyList(),
            allergensFromIngredients = allergensFromIngredients,
            expirationDate = expirationDate,
            selectedImages = selectedImages?.let {
                Json.decodeFromString(it)
            },
            ingredients = ingredients?.let {
                Json.decodeFromString(it)
            },
            nutriments = nutriments?.let {
                Json.decodeFromString(it)
            }
        )
    )
}
