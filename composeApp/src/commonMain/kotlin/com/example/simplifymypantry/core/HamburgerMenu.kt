package com.example.simplifymypantry.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import com.example.simplifymypantry.app.Route
import org.jetbrains.compose.resources.painterResource
import simplifymypantry.composeapp.generated.resources.Res
import simplifymypantry.composeapp.generated.resources.menu_24px

@Composable
fun HamburgerMenu(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = !expanded }) {
        Icon(painter = painterResource(Res.drawable.menu_24px), contentDescription = "Menu")
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        containerColor = MaterialTheme.colorScheme.secondary,

    ) {
        DropdownMenuItem(
            text = { Text("Home")},
            onClick = { navController.navigate(Route.HomeScreen) }
        )
        DropdownMenuItem(
            text = { Text("Pantry") },
            onClick = { navController.navigate(Route.Pantry) }
        )
        DropdownMenuItem(
            text = { Text("Recipes") },
            onClick = { navController.navigate(Route.Recipes) }
        )
        DropdownMenuItem(
            text = { Text("Account") },
            onClick = { navController.navigate(Route.ViewAccount) }
        )
    }
}