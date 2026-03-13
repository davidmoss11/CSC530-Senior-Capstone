package com.example.simplifymypantry.recipe.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simplifymypantry.core.HamburgerMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(viewModel: RecipeDetailViewModel, navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Recipe",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                navigationIcon = { HamburgerMenu(navController) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Recipe Name",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            TextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
            )

            Text(
                text = "Ingredients",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            TextField(
                value = viewModel.ingredients,
                onValueChange = { viewModel.ingredients = it },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth(0.8f).height(120.dp),
                minLines = 3
            )


            Text(
                text = "Instructions",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            TextField(
                value = viewModel.instructions,
                onValueChange = { viewModel.instructions = it },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth(0.8f).height(150.dp),
                minLines = 4
            )

            Spacer(modifier = Modifier.height(20.dp))

            //delete button
            Button(
                onClick = {
                    viewModel.deleteRecipe { navController.popBackStack() }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Delete Recipe")
            }

            Spacer(modifier = Modifier.height(10.dp))

            //save button
            Button(
                onClick = {
                    viewModel.updateRecipe()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Save Changes")
            }
        }
    }
}