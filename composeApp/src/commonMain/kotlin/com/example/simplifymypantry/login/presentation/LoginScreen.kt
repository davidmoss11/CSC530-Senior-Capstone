package com.example.simplifymypantry.login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import simplifymypantry.composeapp.generated.resources.Res
import simplifymypantry.composeapp.generated.resources.textfield_placeholder
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
@Preview
fun LoginScreen (){

    var username by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth(1f)
            .fillMaxHeight(1f),
        verticalArrangement = Arrangement.spacedBy(
            space = 15.dp,
            alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        //use Image to add logo here later
        Text(
            text = "Username",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        TextField(
            value = username,
            onValueChange = { newText: String  -> username = newText
            println(username)},
            placeholder = {
                Text(
                    text = stringResource(Res.string.textfield_placeholder)
                )
            },
            shape = MaterialTheme.shapes.medium
        )
        Text(
            text = "Password",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
        TextField(
            value = password,
            onValueChange = { newText: String ->
                password = newText
                println(password)
            },
            placeholder = {
                Text(
                    text = stringResource(Res.string.textfield_placeholder)
                )
            },
            shape = MaterialTheme.shapes.medium
        )
        Button(
            modifier = Modifier
                .width(120.dp)
                .height(60.dp),
            onClick = {/* fill in later*/ },
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                disabledContainerColor = MaterialTheme.colorScheme.secondary,
                disabledContentColor = MaterialTheme.colorScheme.onSecondary
            ),
            shape = MaterialTheme.shapes.small,
        ){
            Text(
                text = "Login",
                fontSize = 15.sp
                )
        }
        Button(
            modifier = Modifier
                .width(120.dp)
                .height(60.dp),
            onClick = {},
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                disabledContainerColor = MaterialTheme.colorScheme.secondary,
                disabledContentColor = MaterialTheme.colorScheme.onSecondary
            ),
            shape = MaterialTheme.shapes.small,
        ){
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
            )
        }


    }

}

