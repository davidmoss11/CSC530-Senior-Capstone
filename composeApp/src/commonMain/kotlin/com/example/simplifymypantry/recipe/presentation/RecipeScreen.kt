package com.example.simplifymypantry.recipe.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                title = { 
                    Text(
                        "Recipes",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = { HamburgerMenu(navController) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            painter = painterResource(Res.drawable.filter_list_24px),
                            contentDescription = "Filter",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateRecipeClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text(
                    text = "Create New Recipe",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearch(it) },
                label = { Text("Search recipes...", color = Color.Black) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                shape = MaterialTheme.shapes.medium,
                textStyle = TextStyle(color = Color.Black),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray
                )
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recipes) { match ->
                    val recipe = match.recipe
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    recipe.name, 
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Row {
                                    if (match.missingIngredients.isEmpty()) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.primary, 
                                            shape = MaterialTheme.shapes.extraSmall
                                        ) {
                                            Text(
                                                "Can Cook", 
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), 
                                                style = MaterialTheme.typography.labelSmall, 
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    if (recipe.isOfficial) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.secondary, 
                                            shape = MaterialTheme.shapes.extraSmall
                                        ) {
                                            Text(
                                                "OFFICIAL", 
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), 
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSecondary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Text(
                                text = "${recipe.mealType} • $${recipe.price} • ${recipe.rating} stars",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black // High visibility
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Ingredients: ${recipe.ingredients.joinToString(", ")}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black
                            )
                            
                            if (match.missingIngredients.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Missing: ${match.missingIngredients.joinToString(", ")}",
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            if (recipe.reviews.isNotEmpty()) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.primary)
                                Text(
                                    "Last Review:", 
                                    style = MaterialTheme.typography.labelSmall, 
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "\"${recipe.reviews.last()}\"", 
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                Button(
                                    onClick = { viewModel.toggleSaveRecipe(recipe.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (recipe.isSaved) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary,
                                        contentColor = Color.White
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text(
                                        text = if (recipe.isSaved) "❤️ Saved" else "🤍 Save",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { reviewingRecipeId = recipe.id },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = Color.White
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text("Review", fontWeight = FontWeight.Bold)
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
            title = { Text("Write a Review", color = Color.Black) },
            text = {
                OutlinedTextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    label = { Text("Your thoughts", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = Color.Black),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (reviewText.isNotBlank()) {
                            reviewingRecipeId?.let { viewModel.addReview(it, reviewText) }
                        }
                        reviewingRecipeId = null
                    }
                ) {
                    Text("Submit", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { reviewingRecipeId = null }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filter Recipes", color = Color.Black) },
            text = {
                Column {
                    Text("Max Price: $${maxPrice.toInt()}", color = Color.Black)

                    Slider(
                        value = maxPrice.toFloat(),
                        onValueChange = { viewModel.updatePrice(it.toDouble()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary)
                    )

                    OutlinedTextField(
                        value = dietFilter,
                        onValueChange = { viewModel.updateDiet(it) },
                        label = { Text("Dietary Restriction", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.Black),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = typeFilter,
                        onValueChange = { viewModel.updateType(it) },
                        label = { Text("Meal Type", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.Black),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.Gray
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Checkbox(
                            checked = onlyPantry,
                            onCheckedChange = { viewModel.togglePantryFilter() }
                        )
                        Text("Only use my pantry items", color = Color.Black)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Done", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }
}