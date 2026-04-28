package com.example.simplifymypantry.pantry.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.simplifymypantry.core.HamburgerMenu
import com.example.simplifymypantry.pantry.domain.PantryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryScreen(viewModel: PantryViewModel, navController: NavController) {
    val filteredItems by viewModel.filteredItems.collectAsStateWithLifecycle(initialValue = emptyList())
    val filterQuery by viewModel.filterQuery.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<PantryItem?>(null) }
    var itemToDelete by remember { mutableStateOf<PantryItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Pantry") },
                navigationIcon = { HamburgerMenu(navController) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            TextField(
                value = filterQuery,
                onValueChange = { viewModel.updateFilter(it) },
                label = {
                    Text(
                        "Filter by name, category, or diet",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            LazyColumn {
                items(filteredItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.productName ?: "Unnamed Item",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (!item.quantity.isNullOrBlank()) {
                                    Text("Qty: ${item.quantity}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                } else if (item.productQuantity != null) {
                                    Text("Qty: ${item.productQuantity} ${item.productQuantityUnit ?: ""}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                if (!item.foodGroups.isNullOrBlank()) {
                                    Text("Food Groups: ${item.foodGroups}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                if (!item.categoriesTags.isNullOrEmpty()) {
                                    Text("Category: ${item.categoriesTags.joinToString(", ")}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                if (!item.labelsTags.isNullOrEmpty()) {
                                    Text("Labels: ${item.labelsTags.joinToString(", ")}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                if (!item.allergensTags.isNullOrEmpty()) {
                                    Text("Allergens: ${item.allergensTags.joinToString(", ")}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                if (!item.allergensFromIngredients.isNullOrBlank()) {
                                    Text("Allergen Info: ${item.allergensFromIngredients}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                if (!item.expirationDate.isNullOrBlank()) {
                                    Text("Expires: ${item.expirationDate}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                if (!item.notes.isNullOrBlank()) {
                                    Text("Notes: ${item.notes}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Row {
                                    TextButton(onClick = { itemToEdit = item }) {
                                        Text("Edit", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    TextButton(onClick = { itemToDelete = item }) {
                                        Text("Delete", color = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            PantryItemDialog(
                title = "Add Pantry Item",
                onDismiss = { showAddDialog = false },
                onConfirm = { item ->
                    viewModel.addItem(
                        productName = item.productName,
                        productType = item.productType,
                        productQuantity = item.productQuantity,
                        productQuantityUnit = item.productQuantityUnit,
                        quantity = item.quantity,
                        foodGroups = item.foodGroups,
                        brandsTags = item.brandsTags,
                        categoriesTags = item.categoriesTags,
                        labelsTags = item.labelsTags,
                        selectedImages = item.selectedImages,
                        allergensTags = item.allergensTags,
                        ingredients = item.ingredients,
                        nutriments = item.nutriments,
                        allergensFromIngredients = item.allergensFromIngredients,
                        expirationDate = item.expirationDate,
                        notes = item.notes
                    )
                }
            )
        }

        itemToEdit?.let { item ->
            PantryItemDialog(
                title = "Edit Pantry Item",
                initialItem = item,
                onDismiss = { itemToEdit = null },
                onConfirm = { updatedItem ->
                    viewModel.updateItem(
                        id = item.id,
                        productName = updatedItem.productName,
                        productType = updatedItem.productType,
                        productQuantity = updatedItem.productQuantity,
                        productQuantityUnit = updatedItem.productQuantityUnit,
                        quantity = updatedItem.quantity,
                        foodGroups = updatedItem.foodGroups,
                        brandsTags = updatedItem.brandsTags,
                        categoriesTags = updatedItem.categoriesTags,
                        labelsTags = updatedItem.labelsTags,
                        selectedImages = updatedItem.selectedImages,
                        allergensTags = updatedItem.allergensTags,
                        ingredients = updatedItem.ingredients,
                        nutriments = updatedItem.nutriments,
                        allergensFromIngredients = updatedItem.allergensFromIngredients,
                        expirationDate = updatedItem.expirationDate,
                        notes = updatedItem.notes
                    )
                }
            )
        }

        itemToDelete?.let { item ->
            AlertDialog(
                onDismissRequest = { itemToDelete = null },
                title = { Text("Delete Item", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                text = {
                    Text(
                        "Are you sure you want to delete '${item.productName}'?",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteItem(item.id.toLong())
                        itemToDelete = null
                    }) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { itemToDelete = null }) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            )
        }
    }
}

@Composable
fun PantryItemDialog(
    title: String,
    initialItem: PantryItem? = null,
    onDismiss: () -> Unit,
    onConfirm: (PantryItem) -> Unit
) {
    val scrollState = rememberScrollState()
    var name by remember { mutableStateOf(initialItem?.productName ?: "") }
    var quantity by remember { mutableStateOf(initialItem?.quantity ?: "") }
    var category by remember { mutableStateOf(initialItem?.categoriesTags?.joinToString(",") ?: "") }
    var expiry by remember { mutableStateOf(initialItem?.expirationDate ?: "") }
    var notes by remember { mutableStateOf(initialItem?.notes ?: "") }

    // expanded fields
    var productType by remember { mutableStateOf(initialItem?.productType ?: "") }
    var productQuantity by remember { mutableStateOf(initialItem?.productQuantity?.toString() ?: "") }
    var productQuantityUnit by remember { mutableStateOf(initialItem?.productQuantityUnit ?: "") }
    var foodGroups by remember { mutableStateOf(initialItem?.foodGroups ?: "") }

    var brandsTags by remember { mutableStateOf(initialItem?.brandsTags?.joinToString(",") ?: "") }
    var labelsTags by remember { mutableStateOf(initialItem?.labelsTags?.joinToString(",") ?: "") }
    var allergensTags by remember { mutableStateOf(initialItem?.allergensTags?.joinToString(",") ?: "") }

    var allergensFromIngredients by remember {
        mutableStateOf(initialItem?.allergensFromIngredients ?: "")
    }

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.primary,
        onDismissRequest = onDismiss,
        title = {
            Text(title, color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        text = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
                    .verticalScroll(scrollState)
                    .imePadding()
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                val textFieldColors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSecondary,
                    focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSecondary,
                    cursorColor = MaterialTheme.colorScheme.onSecondary,
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
                )

                val fieldModifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .padding(bottom = 8.dp)

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name", color = MaterialTheme.colorScheme.onSecondary) },
                    colors = textFieldColors,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = fieldModifier
                )

                TextField(
                    value = brandsTags,
                    onValueChange = { brandsTags = it },
                    label = { Text("Brands (comma)", color = MaterialTheme.colorScheme.onSecondary) },
                    colors = textFieldColors,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = fieldModifier
                )

                TextField(
                    value = productType,
                    onValueChange = { productType = it },
                    label = { Text("Product Type", color = MaterialTheme.colorScheme.onSecondary) },
                    colors = textFieldColors,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = fieldModifier
                )

                TextField(
                    value = productQuantity,
                    onValueChange = { productQuantity = it },
                    label = { Text("Total Quantity of Item", color = MaterialTheme.colorScheme.onSecondary) },
                    colors = textFieldColors,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = fieldModifier
                )

                TextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Measured Quantity of each Item", color = MaterialTheme.colorScheme.onSecondary) },
                    colors = textFieldColors,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = fieldModifier
                )

                TextField(
                    value = productQuantityUnit,
                    onValueChange = { productQuantityUnit = it },
                    label = { Text("Measured Unit", color = MaterialTheme.colorScheme.onSecondary) },
                    colors = textFieldColors,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = fieldModifier
                )

                TextField(
                    value = expiry,
                    onValueChange = { expiry = it },
                    label = { Text("Expiration Date", color = MaterialTheme.colorScheme.onSecondary) },
                    colors = textFieldColors,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = fieldModifier
                )

                TextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Categories (comma)", color = MaterialTheme.colorScheme.onSecondary) },
                    colors = textFieldColors,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = fieldModifier
                )

                TextField(
                    value = foodGroups,
                    onValueChange = { foodGroups = it },
                    label = { Text("Food Groups", color = MaterialTheme.colorScheme.onSecondary) },
                    colors = textFieldColors,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = fieldModifier
                )

                TextField(
                    value = labelsTags,
                    onValueChange = { labelsTags = it },
                    label = { Text("Labels (comma)", color = MaterialTheme.colorScheme.onSecondary) },
                    colors = textFieldColors,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = fieldModifier
                )

                TextField(
                    value = allergensTags,
                    onValueChange = { allergensTags = it },
                    label = { Text("Allergens (comma)", color = MaterialTheme.colorScheme.onSecondary) },
                    colors = textFieldColors,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = fieldModifier
                )

                TextField(
                    value = allergensFromIngredients,
                    onValueChange = { allergensFromIngredients = it },
                    label = { Text("Allergen Info", color = MaterialTheme.colorScheme.onSecondary) },
                    colors = textFieldColors,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = fieldModifier
                )

                TextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes", color = MaterialTheme.colorScheme.onSecondary) },
                    colors = textFieldColors,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = fieldModifier
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        PantryItem(
                            id = initialItem?.id ?: "",
                            productName = name,
                            quantity = quantity,
                            categoriesTags = category.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                            expirationDate = expiry.ifBlank { null },
                            notes = notes.ifBlank { "" },

                            productType = productType.ifBlank { null },
                            productQuantity = productQuantity.toDoubleOrNull(),
                            productQuantityUnit = productQuantityUnit.ifBlank { null },
                            foodGroups = foodGroups.ifBlank { null },

                            brandsTags = brandsTags.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                            labelsTags = labelsTags.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                            allergensTags = allergensTags.split(",").map { it.trim() }.filter { it.isNotEmpty() },

                            allergensFromIngredients = allergensFromIngredients.ifBlank { null },

                            selectedImages = initialItem?.selectedImages,
                            ingredients = initialItem?.ingredients,
                            nutriments = initialItem?.nutriments
                        )
                    )
                    onDismiss()
                },
                enabled = name.isNotBlank()
            ) {
                Text(if (initialItem == null) "Add" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
