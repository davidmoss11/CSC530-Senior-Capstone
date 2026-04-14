package com.example.simplifymypantry.recipe.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.simplifymypantry.pantry.presentation.PantryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(onCreateRecipeClick : () -> Unit, viewModel: RecipeViewModel, pantryViewModel: PantryViewModel, navController: NavController) {
    val pantryItems by pantryViewModel.items.collectAsStateWithLifecycle()
    val recipes by viewModel.getFilteredRecipes(pantryItems).collectAsStateWithLifecycle(initialValue = emptyList())
    
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val maxPrice by viewModel.maxPrice.collectAsStateWithLifecycle()
    val dietFilter by viewModel.dietFilter.collectAsStateWithLifecycle()
    val typeFilter by viewModel.typeFilter.collectAsStateWithLifecycle()
    val onlyPantry by viewModel.onlyPantry.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var reviewingRecipeId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipes") },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Text("Filter")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateRecipeClick,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Text(
                    text = "Create New Recipe",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearch(it) },
                label = { Text("Search recipes...") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            LazyColumn {
                items(recipes) { recipe ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(recipe.name, style = MaterialTheme.typography.headlineSmall)
                                if (recipe.isOfficial) {
                                    Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.small) {
                                        Text("Official", modifier = Modifier.padding(horizontal = 4.dp), style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                            Text("Type: ${recipe.mealType} | Price: $${recipe.price}")
                            Text("Rating: ${recipe.rating} stars")
                            Text("Ingredients: ${recipe.ingredients.joinToString(", ")}")
                            
                            if (recipe.reviews.isNotEmpty()) {
                                Text("Recent Review: ${recipe.reviews.last()}", style = MaterialTheme.typography.bodySmall)
                            }

                            Row {
                                Button(onClick = { viewModel.toggleSaveRecipe(recipe.id) }) {
                                    Text(if (recipe.isSaved) "Saved" else "Save")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { reviewingRecipeId = recipe.id }) {
                                    Text("Review")
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    if (reviewingRecipeId != null) {
        var reviewText by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { reviewingRecipeId = null },
            title = { Text("Write a Review") },
            text = {
                TextField(value = reviewText, onValueChange = { reviewText = it }, label = { Text("Your review") })
            },
            confirmButton = {
                Button(onClick = {
                    reviewingRecipeId?.let { viewModel.addReview(it, reviewText) }
                    reviewingRecipeId = null
                }) { Text("Submit") }
            }
        )
    }

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filter Recipes") },
            text = {
                Column {
                    Text("Max Price: $${maxPrice.toInt()}")
                    Slider(value = maxPrice.toFloat(), onValueChange = { viewModel.updatePrice(it.toDouble()) }, valueRange = 0f..100f)
                    
                    TextField(value = dietFilter, onValueChange = { viewModel.updateDiet(it) }, label = { Text("Dietary Restriction") }, modifier = Modifier.fillMaxWidth())
                    TextField(value = typeFilter, onValueChange = { viewModel.updateType(it) }, label = { Text("Meal Type") }, modifier = Modifier.fillMaxWidth())
                    
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Checkbox(checked = onlyPantry, onCheckedChange = { viewModel.togglePantryFilter() })
                        Text("Only what's in my pantry")
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showFilterDialog = false }) { Text("Done") }
            }
        )
    }

    if (showAddDialog) {
        var title by remember { mutableStateOf("") }
        var ingredients by remember { mutableStateOf("") }
        var instructions by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("") }

        if (showAddDialog) {
            var title by remember { mutableStateOf("") }
            var ingredients by remember { mutableStateOf("") }
            var instructions by remember { mutableStateOf("") }
            var category by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add New Recipe") },
                text = {
                    Column {
                        TextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Title") }
                        )
                        TextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Category") }
                        )
                        TextField(
                            value = ingredients,
                            onValueChange = { ingredients = it },
                            label = { Text("Ingredients (comma separated)") }
                        )
                        TextField(
                            value = instructions,
                            onValueChange = { instructions = it },
                            label = { Text("Instructions") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        viewModel.addRecipe(title, ingredients, instructions, category)
                        showAddDialog = false
                    }) {
                        Text("Add")
                    }
                }
            )
        }
    }
}


