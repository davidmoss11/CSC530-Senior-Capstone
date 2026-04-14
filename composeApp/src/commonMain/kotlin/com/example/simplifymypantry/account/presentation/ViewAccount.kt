package com.example.simplifymypantry.account.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simplifymypantry.app.Route
import com.example.simplifymypantry.core.HamburgerMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAccount(viewModel : ViewAccountViewModel, navController : NavController, token : String) {

    LaunchedEffect(Unit) {
        viewModel.checkLoginStatus()
    }

    when {
        !viewModel.isCheckComplete -> {
            // Show loading spinner while check is in progress
            CircularProgressIndicator()
        }
        viewModel.isLoggedIn -> {
            // Show account info
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("My Account")
                        }},
                        navigationIcon = { HamburgerMenu(navController) },
                        colors = TopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            scrolledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
                            subtitleContentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                },
                bottomBar = {},

                ) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth(1f)
                        .fillMaxHeight(1f),
                    verticalArrangement = Arrangement.spacedBy(
                        space = 15.dp,
                        alignment = Alignment.CenterVertically
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(viewModel.isLoggedIn){
                        Card(
                            modifier = Modifier
                                .padding(20.dp)
                                .width(200.dp)
                                .align(Alignment.CenterHorizontally),
                            colors = CardColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = MaterialTheme.colorScheme.secondary,
                            )
                        ){
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                Text(text = "Username ")
                                Text(text = viewModel.username ?: "Loading...")
                                Button(
                                    modifier = Modifier
                                        .width(180.dp),
                                    onClick = { viewModel.editUsername() }
                                ) {
                                    Text(text = "Change Username")
                                }
                            }
                        }

                        Card(
                            modifier = Modifier
                                .padding(20.dp)
                                .width(200.dp)
                                .align(Alignment.CenterHorizontally),
                            colors = CardColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = MaterialTheme.colorScheme.secondary,
                            )
                        ){
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                Text(text = "Email ")
                                Text(text = viewModel.email ?: "Loading...")
                                Button(
                                    modifier = Modifier
                                        .width(180.dp),
                                    onClick = { viewModel.editEmail() }
                                ) {
                                    Text(text = "Change Email")
                                }
                            }
                        }
                        Button(
                            modifier = Modifier.width(180.dp),
                            onClick = { navController.navigate(Route.Household) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                text = "My Household",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Button(modifier = Modifier
                            .width(180.dp),
                            onClick = { viewModel.editPassword()}
                        ) {
                            //change password
                            Text(
                                text = "Change password",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Button(
                            modifier = Modifier
                                .width(180.dp),
                            onClick = { viewModel.deleteAccount()}
                        ) {
                            //delete AccountDatabase
                            Text(
                                text = "Delete Account",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    else{
                        Text( text = "Account not Logged In")
                        Button(
                            modifier = Modifier
                                .width(180.dp),
                            onClick = {navController.navigate(Route.LoginPage)}
                        ){
                            Text(
                                text = "Login",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Button(
                            modifier = Modifier
                                .width(180.dp),
                            onClick = {navController.navigate(Route.CreateAccount)}
                        ){
                            Text(
                                text = "Create Account",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                }
            }
        }
        else -> {
            // Show login button
            Button(onClick = { navController.navigate(Route.LoginPage) }) {
                Text("Log In")
            }
        }
    }

    if (viewModel.showEditDialog) {
        EditAccountDialog(
            viewModel = viewModel,
            token = token
        )
    }
}

@Composable
fun EditAccountDialog(
    viewModel: ViewAccountViewModel,
    token: String
) {
    AlertDialog(
        onDismissRequest = { viewModel.showEditDialog = false },
        title = {
            Text(
                when (viewModel.editDialogType) {
                    ViewAccountViewModel.EditType.USERNAME -> "Edit Username"
                    ViewAccountViewModel.EditType.EMAIL -> "Edit Email"
                    ViewAccountViewModel.EditType.PASSWORD -> "Edit Password"
                }
            )
        },
        text = {
            TextField(
                value = viewModel.editDialogValue,
                onValueChange = { viewModel.editDialogValue = it },
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.confirmEdit()
                    viewModel.showEditDialog = false
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = { viewModel.showEditDialog = false }
            ) {
                Text("Cancel")
            }
        }
    )
}