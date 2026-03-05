package com.example.simplifymypantry.account.presentation

import com.example.simplifymypantry.account.presentation.ViewAccountViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable

@Composable
fun ViewAccount(viewModel : ViewAccountViewModel){

     Box(modifier = Modifier.fillMaxSize()

     ){
         Column(

         ){
             Text(text = "Username ")
             Text(text = viewModel.userName ?: "Loading...")
             Button(
                 onClick = {}
             ){
                 Text(text = "Change Username")
             }

             Text(text = "Email ")
             Text(text = viewModel.email ?: "Loading...")
             Button(
                 onClick = {}
             ){
                 Text(text = "Change Email")
             }

             Text(text = "Name")
             Text(text = viewModel.name ?: "Loading...")
             Button(
                 onClick = {}
             ){
                 Text(text = "Change Name")
             }

             Button(
                 onClick = {}
             ){
                 //change password
                 Text(text = "Change password",
                     style = MaterialTheme.typography.bodyMedium)
             }
             Button(
                 onClick = {}
             ){
                //delete account
                 Text(text = "Delete Account",
                     style = MaterialTheme.typography.bodyMedium)
             }
         }
     }
}
