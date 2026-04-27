package com.example.simplifymypantry.account.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import com.example.simplifymypantry.account.presentation.ViewAccountViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simplifymypantry.app.Route
import com.example.simplifymypantry.core.HamburgerMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAccount(viewModel : ViewAccountViewModel, navController : NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Account") },
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
                            Text(text = viewModel.userName ?: "Loading...")
                            Button(
                                modifier = Modifier
                                    .width(180.dp),
                                onClick = {}
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
                                onClick = {}
                            ) {
                                Text(text = "Change Email")
                            }
                        }
                    }

                    Button(modifier = Modifier
                        .width(180.dp),
                        onClick = {}
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
                        onClick = {}
                    ) {
                        //delete account
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
