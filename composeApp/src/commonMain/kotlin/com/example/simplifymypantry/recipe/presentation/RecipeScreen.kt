package com.example.simplifymypantry.recipe.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.simplifymypantry.core.HamburgerMenu
import com.example.simplifymypantry.pantry.presentation.PantryViewModel
import org.jetbrains.compose.resources.painterResource
import simplifymypantry.composeapp.generated.resources.Res
import simplifymypantry.composeapp.generated.resources.filter_list_24px

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
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
                    subtitleContentColor = MaterialTheme.colorScheme.onSecondary),
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            painter = painterResource(Res.drawable.filter_list_24px),
                            contentDescription = "Filter",
                            tint = Color.White
                        )
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
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary

                )
            )

            LazyColumn {
                items(recipes) { recipe ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                        ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(recipe.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                                if (recipe.isOfficial) {
                                    Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.small) {
                                        Text("Official", modifier = Modifier.padding(horizontal = 4.dp), style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                            Text(
                                "Type: ${recipe.mealType} | Price: $${recipe.price}",
                                color = MaterialTheme.colorScheme.onSecondary
                                )
                            Text(
                                "Rating: ${recipe.rating} stars",
                                color = MaterialTheme.colorScheme.onSecondary
                                )
                            Text(
                                "Ingredients: ${recipe.ingredients.joinToString(", ")}",
                                color = MaterialTheme.colorScheme.onSecondary
                                )
                            
                            if (recipe.reviews.isNotEmpty()) {
                                Text(
                                    "Recent Review: ${recipe.reviews.last()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondary
                                    )
                            }

                            Row {
                                Button(onClick = { viewModel.toggleSaveRecipe(recipe.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary
                                    )
                                    ) {
                                    Text(
                                        if (recipe.isSaved) "Saved" else "Save")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { reviewingRecipeId = recipe.id },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary
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
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.onSecondary,
            textContentColor = MaterialTheme.colorScheme.onSecondary,
            title = {
                Text("Write a Review")
            },
            text = {
                TextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    label = { Text("Your review") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        reviewingRecipeId?.let { viewModel.addReview(it, reviewText) }
                        reviewingRecipeId = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Submit")
                }
            }
        )
    }

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.onSecondary,
            textContentColor = MaterialTheme.colorScheme.onSecondary,
            title = {
                Text("Filter Recipes")
            },
            text = {
                Column {
                    Text("Max Price: $${maxPrice.toInt()}")

                    Slider(
                        value = maxPrice.toFloat(),
                        onValueChange = { viewModel.updatePrice(it.toDouble()) },
                        valueRange = 0f..100f
                    )

                    TextField(
                        value = dietFilter,
                        onValueChange = { viewModel.updateDiet(it) },
                        label = { Text("Dietary Restriction") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.primary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )

                    TextField(
                        value = typeFilter,
                        onValueChange = { viewModel.updateType(it) },
                        label = { Text("Meal Type") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.primary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = onlyPantry,
                            onCheckedChange = { viewModel.togglePantryFilter() }
                        )
                        Text("Only what's in my pantry")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showFilterDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Done")
                }
            }
        )
    }
}
