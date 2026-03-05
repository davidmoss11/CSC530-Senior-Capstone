package com.example.simplifymypantry.account.presentation

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

class CreateAccountViewModel : ViewModel() {

    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var showDialog by mutableStateOf(false)
    var dialogMessage by mutableStateOf("")

    fun createAccountClicked(){
        if(username == "" || email == "" || password == "" || confirmPassword == "")
            showDialog = true
            dialogMessage = "Please fill in all fields"


    }

}