package com.example.simplifymypantry.recipe.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.simplifymypantry.pantry.presentation.PantryViewModel
import com.example.simplifymypantry.core.HamburgerMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    onCreateRecipeClick: () -> Unit,
    viewModel: RecipeScreenViewModel,
    pantryViewModel: PantryViewModel,
    navController: NavController
) {
    val pantryItems by pantryViewModel.items.collectAsStateWithLifecycle()
    val recipes by viewModel.getFilteredRecipes(pantryItems).collectAsStateWithLifecycle(initialValue = emptyList())
    
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val maxPrice by viewModel.maxPrice.collectAsStateWithLifecycle()
    val dietFilter by viewModel.dietFilter.collectAsStateWithLifecycle()
    val typeFilter by viewModel.typeFilter.collectAsStateWithLifecycle()
    val onlyPantry by viewModel.onlyPantry.collectAsStateWithLifecycle()

    var showFilterDialog by remember { mutableStateOf(false) }
    var reviewingRecipeId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipes") },
                navigationIcon = { HamburgerMenu(navController) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary),
                actions = {
                    TextButton(onClick = { showFilterDialog = true }) {
                        Text("Filter", color = MaterialTheme.colorScheme.onPrimary)
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
                items(recipes) { match ->
                    val recipe = match.recipe
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    recipe.name, 
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Row {
                                    if (match.missingIngredients.isEmpty()) {
                                        Surface(
                                            color = Color(0xFF4CAF50), 
                                            shape = MaterialTheme.shapes.small
                                        ) {
                                            Text(
                                                "Can Cook Now", 
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), 
                                                style = MaterialTheme.typography.labelSmall, 
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    if (recipe.isOfficial) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.secondary, 
                                            shape = MaterialTheme.shapes.small
                                        ) {
                                            Text(
                                                "Official", 
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), 
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSecondary
                                            )
                                        }
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Text("Type: ${recipe.mealType} | Price: $${recipe.price}", color = Color.LightGray)
                            Text("Rating: ${recipe.rating} stars", color = Color.LightGray)
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text("Ingredients:", fontWeight = FontWeight.SemiBold)
                            Text(recipe.ingredients.joinToString(", "), color = Color.LightGray)
                            
                            if (match.missingIngredients.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Missing: ${match.missingIngredients.joinToString(", ")}",
                                    color = Color(0xFFFF5252), // Bright red for black background
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            if (recipe.reviews.isNotEmpty()) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.DarkGray)
                                Text("Recent Review:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.LightGray)
                                Text("\"${recipe.reviews.last()}\"", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                OutlinedButton(
                                    onClick = { viewModel.toggleSaveRecipe(recipe.id) },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White)
                                ) {
                                    Text(if (recipe.isSaved) "❤️ Saved" else "🤍 Save")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { reviewingRecipeId = recipe.id },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    )
                                ) {
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
                TextField(
                    value = reviewText, 
                    onValueChange = { reviewText = it }, 
                    label = { Text("Your review") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    reviewingRecipeId?.let { viewModel.addReview(it, reviewText) }
                    reviewingRecipeId = null
                }) { Text("Submit") }
            },
            dismissButton = {
                TextButton(onClick = { reviewingRecipeId = null }) { Text("Cancel") }
            }
        )
    }

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filter Recipes") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column {
                        Text("Max Price: $${maxPrice.toInt()}")
                        Slider(
                            value = maxPrice.toFloat(), 
                            onValueChange = { viewModel.updatePrice(it.toDouble()) }, 
                            valueRange = 0f..100f
                        )
                    }
                    
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
}
