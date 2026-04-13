package com.example.simplifymypantry.pantry.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.simplifymypantry.core.HamburgerMenu
import com.example.simplifymypantry.pantry.domain.PantryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryScreen(viewModel: PantryViewModel, navController: NavController) {
    val filteredItems by viewModel.filteredItems.collectAsStateWithLifecycle(initialValue = emptyList())
    val filterQuery by viewModel.filterQuery.collectAsStateWithLifecycle()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<PantryItem?>(null) }
    var itemToDelete by remember { mutableStateOf<PantryItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Pantry") },
                navigationIcon = { HamburgerMenu(navController) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            TextField(
                value = filterQuery,
                onValueChange = { viewModel.updateFilter(it) },
                label = { Text("Filter by name, category, or diet", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            LazyColumn {
                items(filteredItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.name, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                if (item.quantity.isNotBlank()) Text("Qty: ${item.quantity}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                if (item.category.isNotBlank()) Text("Category: ${item.category}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                if (item.expirationDate.isNotBlank()) Text("Expires: ${item.expirationDate}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                if (item.dietInfo.isNotBlank()) Text("Diet: ${item.dietInfo}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                if (item.notes.isNotBlank()) Text("Notes: ${item.notes}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Column {
                                TextButton(onClick = { itemToEdit = item }) {
                                    Text("Edit", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                TextButton(onClick = { itemToDelete = item }) {
                                    Text("Delete", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        PantryItemDialog(
            title = "Add Pantry Item",
            onDismiss = { showAddDialog = false },
            onConfirm = { name, qty, cat, exp, diet, notes ->
                viewModel.addItem(name, qty, cat, exp, diet, notes)
            }
        )
    }

    itemToEdit?.let { item ->
        PantryItemDialog(
            title = "Edit Pantry Item",
            initialItem = item,
            onDismiss = { itemToEdit = null },
            onConfirm = { name, qty, cat, exp, diet, notes ->
                viewModel.updateItem(item.id.toLong(), name, qty, cat, exp, diet, notes)
            }
        )
    }

    itemToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text("Delete Item", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            text = { Text("Are you sure you want to delete '${item.name}'?", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteItem(item.id.toLong())
                    itemToDelete = null
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}

@Composable
fun PantryItemDialog(
    title: String,
    initialItem: PantryItem? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialItem?.name ?: "") }
    var quantity by remember { mutableStateOf(initialItem?.quantity ?: "") }
    var category by remember { mutableStateOf(initialItem?.category ?: "") }
    var expiry by remember { mutableStateOf(initialItem?.expirationDate ?: "") }
    var diet by remember { mutableStateOf(initialItem?.dietInfo ?: "") }
    var notes by remember { mutableStateOf(initialItem?.notes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Name (Required)", color = MaterialTheme.colorScheme.onSurfaceVariant) })
                TextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Quantity (Optional)", color = MaterialTheme.colorScheme.onSurfaceVariant) })
                TextField(value = category, onValueChange = { category = it }, label = { Text("Category (Optional)", color = MaterialTheme.colorScheme.onSurfaceVariant) })
                TextField(value = expiry, onValueChange = { expiry = it }, label = { Text("Expiry (Optional)", color = MaterialTheme.colorScheme.onSurfaceVariant) })
                TextField(value = diet, onValueChange = { diet = it }, label = { Text("Diet Info (Optional)", color = MaterialTheme.colorScheme.onSurfaceVariant) })
                TextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes (Optional)", color = MaterialTheme.colorScheme.onSurfaceVariant) })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(name, quantity, category, expiry, diet, notes)
                    onDismiss()
                },
                enabled = name.isNotBlank()
            ) {
                Text(if (initialItem == null) "Add" else "Save", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
