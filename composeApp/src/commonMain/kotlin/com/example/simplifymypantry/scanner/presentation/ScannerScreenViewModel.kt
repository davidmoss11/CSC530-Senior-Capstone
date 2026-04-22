package com.example.simplifymypantry.scanner.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplifymypantry.scanner.data.Scanner
import kotlinx.coroutines.launch
import co.touchlab.kermit.Logger
import com.example.simplifymypantry.scanner.data.ImageSaver
import com.example.simplifymypantry.scanner.data.OpenFoodFactsAPI
import com.example.simplifymypantry.scanner.data.PantryItemCache
import com.example.simplifymypantry.scanner.data.ProductResponse
import com.example.simplifymypantry.scanner.data.ScannerItemEntity
import com.example.simplifymypantry.scanner.data.SelectedImages
import com.example.simplifymypantry.scanner.data.toProductResponse
import com.example.simplifymypantry.scanner.data.toScannerEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes

class ScannerScreenViewModel(
    val scanner: Scanner,
    val api: OpenFoodFactsAPI,
    val scannerDatabase: PantryItemCache,
    val imageSaver: ImageSaver
) : ViewModel() {

    val log = Logger.withTag("ScannerViewModel")

    var imageError by mutableStateOf<String?>(null)
    var cache by mutableStateOf<ScannerItemEntity?>(null)

    var fromCache by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    var result by mutableStateOf<ProductResponse?>(null)

    var popupDialog by mutableStateOf(false)

    var localImagePath by mutableStateOf<String?>(null)

    val displayImagePath: String?
        get() = localImagePath ?: result?.product?.selectedImages?.front?.display?.values?.firstOrNull()

    init {
        scannerDatabase.pantryItemCacheQueries.clearItems() //delete later
        log.d("ViewModel init, scanner instance: $scanner")
        viewModelScope.launch {
            log.d("Starting collection")
            scanner.scannedCodes.collect { code ->
                log.d("Collected code: $code")
                stop() //stops scanning
                scanner.clearLastCode()

                val normalizedCode = code.padStart(13, '0')
                cache = scannerDatabase.pantryItemCacheQueries
                    .findItem(normalizedCode)
                    .executeAsOneOrNull()
                isLoading = true

                if (cache == null){ //if the code is not in localDB
                    log.d("Item not cached, fetching API")
                    result = api.getProduct(code) //set result to the API code result
                    cache = result?.toScannerEntity() //set cache to a flattened result so that it fits in the local database
                }
                else { //if a local object is found
                    log.d("local cache found ${cache?.productName}")
                    fromCache = true //set from cache true so we don't resave the result
                    localImagePath = imageSaver.getImagePath(normalizedCode, "front_display")
                    log.d("Local image path: $localImagePath")
                    result = (cache as ScannerItemEntity).toProductResponse()
                }
                isLoading = false
                popupDialog = true
            }
        }
    }

    fun confirmDialog() {

        log.d("Image Path $displayImagePath")
        popupDialog = false

        if (!fromCache) {
            log.d("Saving to localDB")
            cache?.let { product ->
                viewModelScope.launch {
                    // Download images and build local path map
                    result?.product?.selectedImages?.let { images ->
                        downloadImages(images, product.code)
                    }

                    // Build a local path JSON string to replace the URLs
                    val localImages = """
                    {"front":{
                        "display":{"local":"${imageSaver.getImagePath(product.code, "front_display")}"},
                        "small":{"local":"${imageSaver.getImagePath(product.code, "front_small")}"},
                        "thumb":{"local":"${imageSaver.getImagePath(product.code, "front_thumb")}"}
                    },"ingredients":{
                        "display":{"local":"${imageSaver.getImagePath(product.code, "ingredients_display")}"},
                        "small":{"local":"${imageSaver.getImagePath(product.code, "ingredients_small")}"},
                        "thumb":{"local":"${imageSaver.getImagePath(product.code, "ingredients_thumb")}"}
                    },"packaging":{
                        "display":{"local":"${imageSaver.getImagePath(product.code, "packaging_display")}"},
                        "small":{"local":"${imageSaver.getImagePath(product.code, "packaging_small")}"},
                        "thumb":{"local":"${imageSaver.getImagePath(product.code, "packaging_thumb")}"}
                    }}
                """.trimIndent()

                    log.d("About to save: code=${product.code}, name=${product.productName}")
                    try {
                        scannerDatabase.pantryItemCacheQueries.insertItem(
                            code = product.code,
                            productType = product.productType,
                            productName = product.productName,
                            productQuantity = product.productQuantity,
                            productQuantityUnit = product.productQuantityUnit,
                            quantity = product.quantity,
                            foodGroups = product.foodGroups,
                            brandsTags = product.brandsTags,
                            categoriesTags = product.categoriesTags,
                            labelsTags = product.labelsTags,
                            allergensTags = product.allergensTags,
                            allergensFromIngredients = product.allergensFromIngredients,
                            expirationDate = product.expirationDate,
                            selectedImages = localImages,  // local paths instead of URLs
                            ingredients = product.ingredients,
                            nutriments = product.nutriments
                        )
                        localImagePath = imageSaver.getImagePath(product.code, "front_display")
                        log.d("Insert executed, local path: $localImagePath")
                    } catch (e: Exception) {
                        log.e("Insert failed: ${e.message} ${e.cause}")
                    }
                    saveItemDialog()
                    cache = null
                    result = null
                    fromCache = false
                    localImagePath = null
                }
            }
        } else {
            saveItemDialog()
            cache = null
            result = null
            fromCache = false
        }
    }
    fun dismissDialog() {
        popupDialog = false
        start()
    }
    fun start() {
        scanner.startScanning()
    }
    fun stop() {
        scanner.stopScanning()
    }

    private suspend fun downloadImages(images: SelectedImages, code: String) {
        val client = HttpClient()

        suspend fun downloadOne(url: String, name: String) {
            try {
                val bytes = client.get(url).readRawBytes()
                imageSaver.saveImage(code, name, bytes)
                log.d("Saved $name for $code")
            } catch (e: Exception) {
                log.e("Failed to download $name: ${e.message}")
            }
        }

        images.front?.display?.entries?.firstOrNull()?.let { downloadOne(it.value, "front_display") }
        images.front?.small?.entries?.firstOrNull()?.let { downloadOne(it.value, "front_small") }
        images.front?.thumb?.entries?.firstOrNull()?.let { downloadOne(it.value, "front_thumb") }

        images.ingredients?.display?.entries?.firstOrNull()?.let { downloadOne(it.value, "ingredients_display") }
        images.ingredients?.small?.entries?.firstOrNull()?.let { downloadOne(it.value, "ingredients_small") }
        images.ingredients?.thumb?.entries?.firstOrNull()?.let { downloadOne(it.value, "ingredients_thumb") }

        images.packaging?.display?.entries?.firstOrNull()?.let { downloadOne(it.value, "packaging_display") }
        images.packaging?.small?.entries?.firstOrNull()?.let { downloadOne(it.value, "packaging_small") }
        images.packaging?.thumb?.entries?.firstOrNull()?.let { downloadOne(it.value, "packaging_thumb") }

        client.close()
    }

    fun saveItemDialog() {

    }
}