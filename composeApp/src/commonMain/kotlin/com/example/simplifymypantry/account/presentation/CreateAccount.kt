package com.example.simplifymypantry.account.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import simplifymypantry.composeapp.generated.resources.Res
import simplifymypantry.composeapp.generated.resources.close_24px
import simplifymypantry.composeapp.generated.resources.visibility_24px
import simplifymypantry.composeapp.generated.resources.visibility_off_24px

@Composable
fun CreateAccount(viewModel: CreateAccountViewModel, onSignIn: () -> Unit, onSkip: () -> Unit){

    var passwordVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxWidth(1f)
                .fillMaxHeight(1f),
            verticalArrangement = Arrangement.spacedBy(
                space = 15.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Create an Account",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Username",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            TextField(
                value = viewModel.username,
                onValueChange = { newText: String ->
                    viewModel.username = newText
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 22.sp
                ),
                placeholder = {
                    Text(
                        text = "Enter Text...",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                shape = MaterialTheme.shapes.medium,
            )
            Text(
                text = "Email Address",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            TextField(
                value = viewModel.email,
                onValueChange = {newText: String ->
                    viewModel.email = newText

                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 22.sp
                ),
                placeholder = {
                    Text(
                        text = "Enter Text...",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                shape = MaterialTheme.shapes.medium
            )
            Text(
                text = "Password",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            TextField(
                value = viewModel.password,
                onValueChange = { newText : String ->
                    viewModel.password = newText
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 22.sp
                ),
                placeholder = {
                    Text(
                        text = "Enter Text...",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = {passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisible) Res.drawable.visibility_24px
                                else Res.drawable.visibility_off_24px
                            ),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                shape = MaterialTheme.shapes.medium
            )
            Text(
                text = "Confirm Password",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            TextField(
                value = viewModel.confirmPassword,
                onValueChange = { newText : String ->
                    viewModel.confirmPassword = newText
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 22.sp
                ),
                placeholder = {
                    Text(
                        text = "Enter Text...",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = {passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisible) Res.drawable.visibility_24px
                                else Res.drawable.visibility_off_24px
                            ),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                shape = MaterialTheme.shapes.medium
            )
            Button(
                modifier = Modifier
                    .width(120.dp)
                    .height(60.dp),
                onClick = { viewModel.createAccountClicked() },
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }
            Button(
                modifier = Modifier
                    .width(120.dp)
                    .height(60.dp),
                onClick =  onSignIn ,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = "Sign In",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                )
            }

        }

        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 20.dp,
                    end = 10.dp,
                    start = 0.dp,
                    bottom = 0.dp
                )
                .size(40.dp),
            onClick =  onSkip ,
            colors = IconButtonColors(
                containerColor = MaterialTheme.colorScheme.onTertiary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContentColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = MaterialTheme.colorScheme.onPrimary
            ),
        ) {
            Icon(
                painter = painterResource(Res.drawable.close_24px),
                contentDescription = "Close"
            )
        }
    }

    if(viewModel.showDialog){
        AlertDialog(
            onDismissRequest = {
                viewModel.showDialog = false
                viewModel.dialogMessage = ""
            },
            title = {Text(
                text = "Important",
                color = MaterialTheme.colorScheme.onSecondary
            )},
            text = {Text(
                text = viewModel.dialogMessage,
                color = MaterialTheme.colorScheme.onSecondary)},
            confirmButton = {
                Button(
                    onClick = { viewModel.showDialog = false }
                ) {
                    Text(
                        text = "Dismiss",
                        color = MaterialTheme.colorScheme.onSecondary)
                }
            },
        )
    }



}