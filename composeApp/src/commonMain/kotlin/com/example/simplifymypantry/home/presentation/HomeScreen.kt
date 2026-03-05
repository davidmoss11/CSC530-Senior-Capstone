package com.example.simplifymypantry.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip

@Composable
@Preview
fun HomeScreen(viewModel : HomeScreenViewModel, pantryClick : () -> Unit, recipeClick : () -> Unit, scanClick : () -> Unit ) {

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            space = 60.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        //Pantry
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.large)
                .padding(
                    horizontal = 10.dp,
                    vertical = 10.dp)
                .size(260.dp, 150.dp),
            contentAlignment = Alignment.Center

        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    space = 20.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Your Pantry",
                    style = MaterialTheme.typography.titleLarge)
                Button(
                    modifier = Modifier
                        .size(160.dp, 80.dp),
                    onClick = pantryClick,
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary,
                        disabledContentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = "Enter",
                        style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        //Recipes
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.large)
                .padding(
                    horizontal = 10.dp,
                    vertical = 10.dp)
                .size(260.dp, 150.dp),
            contentAlignment = Alignment.Center

        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    space = 20.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Your Recipes",
                    style = MaterialTheme.typography.titleLarge)
                Button(
                    modifier = Modifier
                        .size(160.dp, 180.dp),
                    onClick = recipeClick,
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary,
                        disabledContentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = MaterialTheme.shapes.medium
                ){
                    Text(text = "Enter",
                        style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        //Scan Function
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.large)
                .padding(
                    horizontal = 10.dp,
                    vertical = 10.dp)
                .size(260.dp, 150.dp),
            contentAlignment = Alignment.Center

        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    space = 20.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Scan Items",
                    style = MaterialTheme.typography.titleLarge)
                Button(
                    modifier = Modifier
                        .size(160.dp, 80.dp),
                    onClick = scanClick,
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary,
                        disabledContentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = MaterialTheme.shapes.medium,
                ){
                    Text(text = "Enter",
                        style = MaterialTheme.typography.displayLarge)
                }
            }
        }
    }

}