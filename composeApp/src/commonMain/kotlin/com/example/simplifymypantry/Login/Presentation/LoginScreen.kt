package com.example.simplifymypantry.Login.Presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Vertical
import androidx.compose.runtime.*
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonShapes
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplifymypantry.Core.darkGreen
import com.example.simplifymypantry.Core.white
import org.jetbrains.compose.resources.StringResource
import simplifymypantry.composeapp.generated.resources.Res
import simplifymypantry.composeapp.generated.resources.textfield_placeholder
import com.example.simplifymypantry.Core.darkGreen10b
import com.example.simplifymypantry.Core.darkGreen10w
import com.example.simplifymypantry.Core.darkGreen20b
import com.example.simplifymypantry.Core.darkGreen30b
import com.example.simplifymypantry.Core.darkGreen40b
import org.jetbrains.compose.resources.stringResource
@Composable
@Preview
fun LoginScreen (){

    var username by remember { mutableStateOf("") }
    var password by remember {mutableStateOf("")}

    Column(
        modifier = Modifier
            .background(darkGreen)
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
            style = MaterialTheme.typography.titleSmall,
            color = white,
            fontSize = 25.sp
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
            shape = RoundedCornerShape(10.dp)
        )
        Text(
            text = "Password",
            style = MaterialTheme.typography.titleSmall,
            color = white,
            fontSize = 25.sp
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
            shape = RoundedCornerShape(10.dp)
        )
        Button(
            modifier = Modifier
                .width(120.dp)
                .height(60.dp),
            onClick = {/* fill in later*/ },
            colors = ButtonColors(
                containerColor = darkGreen20b,
                contentColor = white,
                disabledContainerColor = darkGreen20b,
                disabledContentColor = white
            ),
            shape = RoundedCornerShape(10.dp),
        ){
            Text(
                text = "Login",
                fontSize = 20.sp
                )
        }

    }

}

