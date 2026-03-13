package com.example.simplifymypantry.pantry.presentation

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
fun PantryScreen(viewModel: PantryViewModel) {
    val filteredItems by viewModel.filteredItems.collectAsStateWithLifecycle(initialValue = emptyList())
    val filterQuery by viewModel.filterQuery.collectAsStateWithLifecycle()
    
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Pantry") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            // Filter field
            TextField(
                value = filterQuery,
                onValueChange = { viewModel.updateFilter(it) },
                label = { Text("Filter by name, category, or diet") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            LazyColumn {
                items(filteredItems) { item ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(item.name, style = MaterialTheme.typography.headlineSmall)
                            if (item.quantity.isNotBlank()) Text("Qty: ${item.quantity}")
                            if (item.category.isNotBlank()) Text("Category: ${item.category}")
                            if (item.expirationDate.isNotBlank()) Text("Expires: ${item.expirationDate}")
                            if (item.dietInfo.isNotBlank()) Text("Diet: ${item.dietInfo}")
                            if (item.notes.isNotBlank()) Text("Notes: ${item.notes}")
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var name by remember { mutableStateOf("") }
        var quantity by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("") }
        var expiry by remember { mutableStateOf("") }
        var diet by remember { mutableStateOf("") }
        var notes by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Pantry Item") },
            text = {
                Column {
                    TextField(value = name, onValueChange = { name = it }, label = { Text("Name (Required)") })
                    TextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Quantity (Optional)") })
                    TextField(value = category, onValueChange = { category = it }, label = { Text("Category (Optional)") })
                    TextField(value = expiry, onValueChange = { expiry = it }, label = { Text("Expiry (Optional)") })
                    TextField(value = diet, onValueChange = { diet = it }, label = { Text("Diet Info (Optional)") })
                    TextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes (Optional)") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addItem(name, quantity, category, expiry, diet, notes)
                        showAddDialog = false
                    },
                    enabled = name.isNotBlank()
                ) {
                    Text("Add")
                }
            }
        )
    }
}
