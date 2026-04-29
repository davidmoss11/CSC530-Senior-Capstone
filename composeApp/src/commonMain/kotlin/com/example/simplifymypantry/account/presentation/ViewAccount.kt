package com.example.simplifymypantry.account.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simplifymypantry.app.Route
import com.example.simplifymypantry.core.HamburgerMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAccount(viewModel : ViewAccountViewModel, navController : NavController, token: String) {
    var showUsernameDialog by remember { mutableStateOf(false) }
    var showEmailDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var newValue by remember { mutableStateOf("") }

    if (viewModel.showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showDialog = false },
            title = { Text("Account Update", color = Color.Black) },
            text = { Text(viewModel.dialogMessage, color = Color.Black) },
            confirmButton = {
                TextButton(onClick = { viewModel.showDialog = false }) {
                    Text("OK", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    if (showUsernameDialog || showEmailDialog || showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { 
                showUsernameDialog = false
                showEmailDialog = false
                showPasswordDialog = false
                newValue = ""
            },
            title = { Text("Enter New Value", color = Color.Black) },
            text = {
                OutlinedTextField(
                    value = newValue,
                    onValueChange = { newValue = it },
                    placeholder = { Text("Enter here...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (showUsernameDialog) viewModel.updateUsername(newValue)
                    if (showEmailDialog) viewModel.updateEmail(newValue)
                    if (showPasswordDialog) viewModel.updatePassword(newValue)
                    
                    showUsernameDialog = false
                    showEmailDialog = false
                    showPasswordDialog = false
                    newValue = ""
                }) {
                    Text("Save", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showUsernameDialog = false
                    showEmailDialog = false
                    showPasswordDialog = false
                    newValue = ""
                }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "My Account",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = { HamburgerMenu(navController) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(viewModel.isLoggedIn){
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ){
                    Column(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = "Username", style = MaterialTheme.typography.labelMedium, color = Color.Black)
                        Text(
                            text = viewModel.userName, 
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Button(
                            modifier = Modifier.padding(top = 12.dp),
                            onClick = { showUsernameDialog = true }
                        ) {
                            Text("Change Username")
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ){
                    Column(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = "Email", style = MaterialTheme.typography.labelMedium, color = Color.Black)
                        Text(
                            text = viewModel.email, 
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Button(
                            modifier = Modifier.padding(top = 12.dp),
                            onClick = { showEmailDialog = true }
                        ) {
                            Text("Change Email")
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Expiring Food Notifications", fontWeight = FontWeight.Medium, color = Color.Black)
                    Switch(
                        checked = viewModel.pushNotificationsEnabled,
                        onCheckedChange = { viewModel.pushNotificationsEnabled = it }
                    )
                }

                Button(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    onClick = { showPasswordDialog = true }
                ) {
                    Text("Change Password")
                }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.logout()
                        navController.navigate(Route.LoginPage) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text("Logout", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }

                TextButton(
                    onClick = {
                        viewModel.deleteAccount {
                            navController.navigate(Route.LoginPage) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                ) {
                    Text("Delete Account", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Account not Logged In", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { navController.navigate(Route.LoginPage) }) {
                            Text("Go to Login")
                        }
                    }
                }
            }
        }
    }
}
