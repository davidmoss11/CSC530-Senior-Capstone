package com.example.simplifymypantry.recipe.presentation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.Switch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simplifymypantry.core.HamburgerMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRecipeScreen(viewModel: CreateRecipeScreenViewModel, navController: NavController) {
    if (viewModel.showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showDialog = false },
            title = { Text("Recipe Status") },
            text = { Text(viewModel.dialogMessage) },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.showDialog = false
                    if (viewModel.dialogMessage.contains("successfully")) {
                        navController.popBackStack()
                    }
                }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Recipe",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    HamburgerMenu(navController)
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Recipe Name",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                TextField(
                    value = viewModel.recipeName,
                    onValueChange = { viewModel.recipeName = it },
                    placeholder = { Text("Enter recipe name...", color = MaterialTheme.colorScheme.onSecondary) },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (viewModel.isPublic) "Public Recipe" else "Private Recipe",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Switch(
                        checked = viewModel.isPublic,
                        onCheckedChange = { viewModel.isPublic = it }
                    )
                }

                Text(
                    text = "Ingredients",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                TextField(
                    value = viewModel.ingredients,
                    onValueChange = { viewModel.ingredients = it },
                    placeholder = { Text("Enter ingredients...", color = MaterialTheme.colorScheme.onSecondary) },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(0.8f).height(120.dp),
                    minLines = 3
                )

                Text(
                    text = "Instructions" + if (viewModel.isPublic) " (Mandatory)" else " (Optional)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                TextField(
                    value = viewModel.instructions,
                    onValueChange = { viewModel.instructions = it },
                    placeholder = { Text("Enter instructions...", color = MaterialTheme.colorScheme.onSecondary) },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(0.8f).height(150.dp),
                    minLines = 4
                )

                Button(
                    modifier = Modifier
                        .width(150.dp)
                        .height(60.dp)
                        .padding(top = 10.dp),
                    onClick = { viewModel.saveRecipeClicked() },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary,
                        disabledContentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "Save Recipe",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}