package com.example.simplifymypantry.recipe.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(viewModel: RecipeViewModel) {
    val recipes by viewModel.recipes.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Recipes") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            LazyColumn {
                items(recipes) { recipe ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(recipe.title, style = MaterialTheme.typography.headlineSmall)
                            Text("Category: ${recipe.category}")
                            Text("Ingredients: ${recipe.ingredients.joinToString(", ")}")
                            Text("Instructions: ${recipe.instructions}")
                        }
                    }
                }
            }
        }
    }

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
                    TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                    TextField(value = category, onValueChange = { category = it }, label = { Text("Category") })
                    TextField(value = ingredients, onValueChange = { ingredients = it }, label = { Text("Ingredients (comma separated)") })
                    TextField(value = instructions, onValueChange = { instructions = it }, label = { Text("Instructions") })
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
