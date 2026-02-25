package com.example.simplifymypantry.login.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var showDialog by mutableStateOf(false)
    var dialogMessage by mutableStateOf("")

    fun loginButtonClicked() {
        if(username == "" || password == ""){
            showDialog = true
            dialogMessage = "Missing Username or Password"
        }
    }

    fun createAccountButtonClicked() {

    }

    fun cornerXButtonClicked() {

    }

}