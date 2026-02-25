package com.example.simplifymypantry.login.presentation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import simplifymypantry.composeapp.generated.resources.Res
import simplifymypantry.composeapp.generated.resources.textfield_placeholder
import simplifymypantry.composeapp.generated.resources.visibility_24px
import simplifymypantry.composeapp.generated.resources.visibility_off_24px
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.painterResource

@Composable
@Preview
fun LoginScreen (viewModel: LoginViewModel, onCreateAccount: () -> Unit, onSkip: () -> Unit){

    var passwordVisible by remember { mutableStateOf(false)}

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth(1f)
                .fillMaxHeight(1f),
            verticalArrangement = Arrangement.spacedBy(
                space = 15.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //use Image to add logo here later
            Text(
                text = "Sign In",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Username",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
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
                        text = stringResource(Res.string.textfield_placeholder),
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 22.sp
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
                onValueChange = { newText: String ->
                    viewModel.password = newText
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 22.sp
                ),
                placeholder = {
                    Text(
                        text = stringResource(Res.string.textfield_placeholder),
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 22.sp
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
                onClick = { viewModel.loginButtonClicked() },
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Button(
                modifier = Modifier
                    .width(120.dp)
                    .height(60.dp),
                onClick =  onCreateAccount ,
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
            Text(
                text = "X",
                style = MaterialTheme.typography.titleMedium
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

