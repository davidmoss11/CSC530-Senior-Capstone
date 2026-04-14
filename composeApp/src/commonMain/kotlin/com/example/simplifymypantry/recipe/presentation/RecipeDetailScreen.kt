package com.example.simplifymypantry.recipe.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simplifymypantry.core.HamburgerMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(viewModel: RecipeDetailViewModel, navController: NavController) {
    var commentText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Details") },
                navigationIcon = { HamburgerMenu(navController) },
                actions = {
                    // Share Button
                    IconButton(onClick = { viewModel.shareRecipe() }) {
                        Text("Share", style = MaterialTheme.typography.labelSmall)
                    }
                    // Save/Favorite Button
                    IconButton(onClick = { viewModel.toggleSave() }) {
                        Text(if (viewModel.isSaved) "❤️" else "🤍")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = { Text("Recipe Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = viewModel.ingredients,
                onValueChange = { viewModel.ingredients = it },
                label = { Text("Ingredients") },
                modifier = Modifier.fillMaxWidth().height(100.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = viewModel.instructions,
                onValueChange = { viewModel.instructions = it },
                label = { Text("Instructions") },
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Comments Section
            Text("Comments", style = MaterialTheme.typography.titleMedium)
            
            Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
                viewModel.comments.forEach { comment ->
                    Text("${comment.user}: ${comment.text}", style = MaterialTheme.typography.bodySmall)
                }
            }

            // Add Comment Field
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text("Add a comment...") },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = { 
                    viewModel.addComment(commentText)
                    commentText = "" 
                }) {
                    Text("Post")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    viewModel.updateRecipe()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }
        }
    }
}